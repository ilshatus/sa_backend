package com.idc.idc.service;

import com.idc.idc.model.Order;
import com.idc.idc.model.Vehicle;
import com.idc.idc.model.embeddable.CurrentLocation;
import com.idc.idc.model.enums.VehicleType;

import java.util.List;

public interface VehicleService {
    Vehicle getVehicle(Long id);
    List<Vehicle> getAllVehicles();
    List<Vehicle> getAllVehicles(Integer limit, Integer offset);
    List<Vehicle> getVehiclesByType(VehicleType type);
    List<Vehicle> getNearestVehicles(Order order, Integer limit);
    Vehicle updatePositionOfVehicle(Long driverId, CurrentLocation location);
    Vehicle submitVehicle(Vehicle vehicle);
    List<Vehicle> getTracksRequiringDrivers();
    CurrentLocation getVehicleLocation(Vehicle vehicle);
}
