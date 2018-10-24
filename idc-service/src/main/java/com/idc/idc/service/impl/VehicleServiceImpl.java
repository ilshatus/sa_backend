package com.idc.idc.service.impl;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.Order;
import com.idc.idc.model.Task;
import com.idc.idc.model.Vehicle;
import com.idc.idc.model.embeddable.CurrentLocation;
import com.idc.idc.model.embeddable.OrderOrigin;
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

import java.util.ArrayList;
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
    public List<Vehicle> getNearestVehicles(Long orderId, Integer limit) {
        Order order = orderService.getOrder(orderId);
        OrderOrigin orderLoc = order.getOrigin();
        List<Vehicle> vehicles = getAllVehicles();
        List<Pair<Long, Vehicle>> tmp = vehicles.stream()
                .map((Vehicle vehicle)->{
                    CurrentLocation location = vehicle.getLocation();
                    DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext)
                            .origin(new LatLng(location.getLatitude(), location.getLongitude()))
                            .destination(new LatLng(orderLoc.getOriginLatitude(), orderLoc.getOriginLongitude()));
                    try {
                        DirectionsResult result = request.await();
                        Long shortestTime = Long.MAX_VALUE;
                        if (result.routes.length > 0) {
                            for (DirectionsLeg leg : result.routes[0].legs) {
                                shortestTime = Math.min(shortestTime, leg.duration.inSeconds);
                            }
                        }
                        return new Pair<>(shortestTime, vehicle);
                    } catch (Exception e) {
                        return new Pair<>(Long.MAX_VALUE, vehicle);
                    }
                }).collect(Collectors.toList());
        vehicles = tmp.stream().limit(limit).map(Pair::getSecond).collect(Collectors.toList());
        return vehicles;
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

    private Double distance(CurrentLocation loc, OrderOrigin origin) {
        double R = 6371e3; // metres
        double lat1 = loc.getLatitude() * Math.PI / 180;
        double lat2 = origin.getOriginLatitude() * Math.PI / 180;
        double lon1 = loc.getLongitude() * Math.PI / 180;
        double lon2 = origin.getOriginLongitude() * Math.PI / 180;

        double deltaF = lat2 - lat1;
        double deltaLambda = lon2 - lon1;

        double a = Math.sin(deltaF / 2) * Math.sin(deltaF / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
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
}
