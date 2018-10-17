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

import java.util.ArrayList;
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
            Double dist1 = Math.pow(loc1.getLatitude() - orderLoc.getOriginLatitude(), 2) +
                    Math.pow(loc1.getLongitude() - orderLoc.getOriginLongitude(), 2);

            Double dist2 = Math.pow(loc2.getLatitude() - orderLoc.getOriginLatitude(), 2) +
                    Math.pow(loc2.getLongitude() - orderLoc.getOriginLongitude(), 2);
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

    @Override
    public List<Vehicle> getTracksRequiringDrivers(){
        List<Vehicle> tracks = vehicleRepository.findAllByType(VehicleType.TRACK);
        List<Vehicle> tracksRequiringDrivers = new ArrayList<>();
        for(Vehicle track:tracks){
            if(track.getDrivers().size() < 2)
            {
                tracksRequiringDrivers.add(track);
            }
        }
        return tracksRequiringDrivers;
    }
}
