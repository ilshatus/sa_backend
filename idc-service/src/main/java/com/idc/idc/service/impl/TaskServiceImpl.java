package com.idc.idc.service.impl;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.*;
import com.idc.idc.dto.form.CreateTaskForm;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.exception.UnauthorizedException;
import com.idc.idc.model.Order;
import com.idc.idc.model.Task;
import com.idc.idc.model.Vehicle;
import com.idc.idc.model.embeddable.CurrentLocation;
import com.idc.idc.model.embeddable.OrderDestination;
import com.idc.idc.model.embeddable.OrderOrigin;
import com.idc.idc.model.enums.OrderStatus;
import com.idc.idc.model.enums.TaskStatus;
import com.idc.idc.model.users.Driver;
import com.idc.idc.repository.TaskRepository;
import com.idc.idc.service.OrderService;
import com.idc.idc.service.TaskService;
import com.idc.idc.service.UserService;
import com.idc.idc.service.VehicleService;
import com.idc.idc.util.CollectionUtils;
import com.idc.idc.util.Direction;
import com.idc.idc.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.Cache;

import java.util.*;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;
    private UserService userService;
    private OrderService orderService;
    private VehicleService vehicleService;
    private GeoApiContext geoApiContext;

    private Map<Long, Direction> vehiclePositions;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           UserService userService,
                           OrderService orderService,
                           GeoApiContext geoApiContext,
                           VehicleService vehicleService) {
        this.taskRepository = taskRepository;
        this.geoApiContext = geoApiContext;
        this.userService = userService;
        this.orderService = orderService;
        this.vehicleService = vehicleService;
        this.vehiclePositions = new HashMap<>();
    }

    @Override
    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(String.format("Task %d not found", taskId)));
    }

    @Override
    public List<Task> getNotCompleteTasks(Integer limit, Integer offset) {
        List<Task> tasks = new ArrayList<>();
        for (TaskStatus taskStatus : TaskStatus.values()) {
            if (taskStatus.equals(TaskStatus.COMPLETE)) continue;
            List<Task> tmp = getTasksByStatus(taskStatus);
            tasks.addAll(tmp);
        }
        return CollectionUtils.subList(tasks,
                offset * limit, (offset + 1) * limit);
    }

    @Override
    public List<Task> getTasksByVehicle(Vehicle vehicle) {
        return taskRepository.findAllByVehicle(vehicle);
    }

    @Override
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findAllByStatus(status);
    }

    @Override
    public List<Task> getTasksByVehicleAndStatus(Vehicle vehicle, TaskStatus status) {
        return taskRepository.findAllByVehicleAndStatus(vehicle, status);
    }

    @Override
    public List<Task> getTasksByDriverAndStatus(Long driverId, TaskStatus status) {
        Driver driver = userService.getDriver(driverId);
        Vehicle vehicle = driver.getVehicle();
        if (vehicle == null) {
            return Lists.emptyList();
        }
        return getTasksByVehicleAndStatus(vehicle, status);
    }

    @Override
    public Task submitTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task getTaskByOrderAndStatus(Order order, TaskStatus status) {
        return taskRepository.findByOrderAndStatus(order, status)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Task for order %s and with status %s not found", order.getId(), status)));
    }

    @Override
    public Task changeStatus(Long taskId, TaskStatus status, Long driverId) {
        Task task = getTask(taskId);
        Driver driver = userService.getDriver(driverId);
        if (!task.getVehicle().getDrivers().contains(driver)) {
            throw new UnauthorizedException(
                    String.format("Driver %d not authorized to modify task %d", driver.getId(), task.getId()));
        }
        task.setStatus(status);
        return submitTask(task);
    }

    @Transactional
    @Override
    public Task createTask(Long orderId, CreateTaskForm form) {
        Task task = form.toTask();
        Order order = orderService.getOrder(orderId);
        task.setOrder(order);
        orderService.changeStatus(orderId, OrderStatus.IN_PROGRESS);
        Vehicle vehicle = vehicleService.getVehicle(form.getVehicleId());
        task.setVehicle(vehicle);
        List<Driver> drivers = vehicle.getDrivers();
        task.setRouteId(form.getRouteId());
        task.setStepId(0L);
        task.setPercentTravelled(0D);
        task = submitTask(task);
        for (Driver driver : drivers) {
            userService.notifyDriver(driver, task);
        }
        return task;
    }

    @Override
    public void updatePositionOfVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleService.getVehicle(vehicleId);
        List<Task> tasks = getTasksByVehicleAndStatus(vehicle, TaskStatus.IN_PROGRESS);
        if (tasks.isEmpty()) return;
        Task task = tasks.get(0);
        if (task.getPercentTravelled() >= 1.0) {
            vehiclePositions.remove(vehicleId);
            return;
        }
        Direction direction;
        int i = task.getStepId().intValue();
        if (!vehiclePositions.containsKey(vehicleId)) {
            OrderOrigin origin = task.getOrder().getOrigin();
            OrderDestination destination = task.getOrder().getDestination();
            DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext)
                    .mode(TravelMode.DRIVING)
                    .alternatives(true)
                    .origin(new LatLng(origin.getOriginLatitude(), origin.getOriginLongitude()))
                    .destination(new LatLng(destination.getDestinationLatitude(),
                            destination.getDestinationLongitude()));
            try {
                DirectionsResult result = request.await();
                DirectionsLeg directionsLeg = result.routes[task.getRouteId().intValue()].legs[0];
                direction = new Direction();
                for (DirectionsStep directionsStep : directionsLeg.steps) {
                    direction.addPolyline(new Pair<>(directionsStep.distance.inMeters,
                            directionsStep.polyline.decodePath().size()));
                }
                vehiclePositions.put(vehicleId, direction);

                int cnt = i;
                while (cnt > direction.getCurrent().getSecond()) {
                    cnt -= direction.getCurrent().getSecond();
                    direction.popFirst();
                }
                direction.setCurrentStepInStep(cnt);
            } catch (Exception e) {
                return;
            }
        } else {
            direction = vehiclePositions.get(vehicleId);
        }

        Date lastModified = task.getLastModifiedDate();
        long secsSinceLastUpdate = (System.currentTimeMillis() - lastModified.getTime()) / 1000;

        Pair<Long, Integer> currentStep = direction.getCurrent();

        Double requiredDist = 2000.0 * secsSinceLastUpdate;

        Double distOfSmallStep = 1.0 * currentStep.getFirst() / currentStep.getSecond();
        Double leftDistOfStep = currentStep.getFirst() - distOfSmallStep * direction.getCurrentStepInStep();

        while (requiredDist > 0) {
            if (requiredDist < leftDistOfStep) {
                int delta = (int) (requiredDist / distOfSmallStep);
                i += delta;
                direction.setCurrentStepInStep(direction.getCurrentStepInStep() + delta);
                break;
            } else {
                int delta = currentStep.getSecond() - direction.getCurrentStepInStep();
                i += delta;
                requiredDist -= delta * distOfSmallStep;
                direction.popFirst();
                direction.setCurrentStepInStep(0);
                try {
                    currentStep = direction.getCurrent();
                    distOfSmallStep = 1.0 * currentStep.getFirst() / currentStep.getSecond();
                    leftDistOfStep = currentStep.getFirst() - distOfSmallStep * direction.getCurrentStepInStep();
                } catch (Exception e) {
                    log.info("Vehicle {} travelled the route", vehicleId);
                    break;
                }
            }
        }

        task.setStepId((long) i);
        task.setPercentTravelled(1.0 * i / direction.getTotalAmountOfSteps());
        submitTask(task);
    }
}
