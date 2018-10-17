package com.idc.idc.service.impl;

import com.idc.idc.exception.NotFoundException;
import com.idc.idc.exception.UnauthorizedException;
import com.idc.idc.model.Order;
import com.idc.idc.model.Task;
import com.idc.idc.model.Vehicle;
import com.idc.idc.model.enums.TaskStatus;
import com.idc.idc.model.users.Driver;
import com.idc.idc.repository.TaskRepository;
import com.idc.idc.service.TaskService;
import com.idc.idc.service.UserService;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;
    private UserService userService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Override
    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(String.format("Task %d not found", taskId)));
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
}
