package com.idc.idc.service;

import com.idc.idc.dto.form.CreateVehicleForm;
import com.idc.idc.model.Vehicle;
import com.idc.idc.model.embeddable.CurrentLocation;
import com.idc.idc.model.enums.VehicleType;
import com.idc.idc.util.Pair;

import java.util.List;

public interface VehicleService {
    Vehicle createVehicle(CreateVehicleForm form);
    Vehicle getVehicle(Long id);
    List<Vehicle> getAllVehicles();
    List<Vehicle> getVehiclesByType(VehicleType type);
    List<Pair<Long, Vehicle>> getNearestVehicles(Long orderId, Long timeToDeliver);
    Vehicle updatePositionOfVehicle(Long driverId, CurrentLocation location);
    Vehicle submitVehicle(Vehicle vehicle);
    List<Vehicle> getTracksRequiringDrivers();
    Vehicle addDriver(Long vehicleId, Long driverId);
}
