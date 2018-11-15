package com.idc.idc.service.impl;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.Distance;
import com.google.maps.model.LatLng;
import com.idc.idc.dto.form.CreateVehicleForm;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.Order;
import com.idc.idc.model.Task;
import com.idc.idc.model.Vehicle;
import com.idc.idc.model.embeddable.CurrentLocation;
import com.idc.idc.model.embeddable.OrderOrigin;
import com.idc.idc.model.enums.TaskStatus;
import com.idc.idc.model.enums.VehicleType;
import com.idc.idc.model.users.Driver;
import com.idc.idc.repository.VehicleRepository;
import com.idc.idc.service.OrderService;
import com.idc.idc.service.TaskService;
import com.idc.idc.service.UserService;
import com.idc.idc.service.VehicleService;
import com.idc.idc.util.CollectionUtils;
import com.idc.idc.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {
    private VehicleRepository vehicleRepository;
    private UserService userService;
    private GeoApiContext geoApiContext;
    private OrderService orderService;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository,
                              UserService userService,
                              GeoApiContext geoApiContext,
                              OrderService orderService) {
        this.vehicleRepository = vehicleRepository;
        this.userService = userService;
        this.geoApiContext = geoApiContext;
        this.orderService = orderService;
    }

    @Override
    public Vehicle createVehicle(CreateVehicleForm form) {
        return vehicleRepository.save(form.toVehicle());
    }

    @Override
    public Vehicle getVehicle(Long id) {
        return vehicleRepository.findOneById(id).orElseThrow(
                () -> new NotFoundException(String.format("Vehicle %d not found", id))
        );
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public List<Vehicle> getVehiclesByType(VehicleType type) {
        return vehicleRepository.findAllByType(type);
    }

    @Override
    public List<Pair<Long, Vehicle>> getNearestVehicles(Long orderId, Long timeToDeliver) {
        Order order = orderService.getOrder(orderId);
        OrderOrigin orderLoc = order.getOrigin();
        List<Vehicle> vehicles = getAllVehicles();
        return vehicles.stream()
                .map((Vehicle vehicle) -> {
                    CurrentLocation location = vehicle.getLocation();
                    DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext)
                            .origin(new LatLng(location.getLatitude(), location.getLongitude()))
                            .destination(new LatLng(orderLoc.getOriginLatitude(), orderLoc.getOriginLongitude()));
                    try {
                        DirectionsResult result = request.await();
                        Long shortestTime = Long.MAX_VALUE;
                        if (result.routes.length > 0) {
                            for (DirectionsLeg leg : result.routes[0].legs) {
                                Long has = (leg.duration.inSeconds + timeToDeliver) * 1000 + Instant.now().toEpochMilli(); // convert to milliseconds
                                Long need = order.getDueDate().getTime();

                                if (has <= need)
                                    shortestTime = Math.min(shortestTime, leg.duration.inSeconds);
                            }
                        }
                        return new Pair<>(shortestTime, vehicle);
                    } catch (Exception e) {
                        return new Pair<>(Long.MAX_VALUE, vehicle);
                    }
                }).filter(val -> val.getFirst() != Long.MAX_VALUE).sorted().collect(Collectors.toList());
    }

    @Override
    public Vehicle updatePositionOfVehicle(Long driverId, CurrentLocation location) {
        Driver driver = userService.getDriver(driverId);
        if (driver.getVehicle() == null) {
            throw new NotFoundException(String.format("Driver %d do not assigned to vehicle", driver.getId()));
        }
        Vehicle vehicle = driver.getVehicle();
        vehicle.setLocation(location);
        return submitVehicle(vehicle);
    }


    @Override
    public Vehicle submitVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> getTracksRequiringDrivers() {
        List<Vehicle> tracks = getVehiclesByType(VehicleType.TRACK);
        List<Vehicle> tracksRequiringDrivers = new ArrayList<>();
        for (Vehicle track : tracks) {
            if (track.getDrivers().size() < 2) {
                tracksRequiringDrivers.add(track);
            }
        }
        return tracksRequiringDrivers;
    }

    @Override
    public Vehicle addDriver(Long vehicleId, Long driverId) {
        Vehicle vehicle = getVehicle(vehicleId);
        Driver driver = userService.getDriver(driverId);
        driver.setVehicle(vehicle);
        userService.submitDriver(driver);
        return getVehicle(vehicleId);
    }
}
