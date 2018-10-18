package com.idc.idc.service.impl;

import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.Order;
import com.idc.idc.model.Vehicle;
import com.idc.idc.model.embeddable.CurrentLocation;
import com.idc.idc.model.embeddable.OrderOrigin;
import com.idc.idc.model.enums.VehicleType;
import com.idc.idc.model.users.Driver;
import com.idc.idc.repository.VehicleRepository;
import com.idc.idc.service.VehicleService;
import com.idc.idc.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {
    private VehicleRepository vehicleRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
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
    public List<Vehicle> getNearestVehicles(Order order, Integer limit) {
        List<Vehicle> drivers = getAllVehicles();
        OrderOrigin orderLoc = order.getOrigin();
        drivers.sort((Vehicle o1, Vehicle o2) -> {
            CurrentLocation loc1 = o1.getLocation();
            CurrentLocation loc2 = o2.getLocation();
            Double dist1 = distance(loc1, orderLoc);
            Double dist2 = distance(loc2, orderLoc);
            if (dist1.equals(dist2))
                return 0;
            if (dist1 < dist2) {
                return 1;
            } else {
                return -1;
            }
        });
        return CollectionUtils.subList(drivers, 0, limit);
    }

    private Double distance(CurrentLocation loc, OrderOrigin origin){
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
}
