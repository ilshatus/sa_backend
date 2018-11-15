package com.idc.idc.jobs;

import com.idc.idc.model.Vehicle;
import com.idc.idc.service.TaskService;
import com.idc.idc.service.VehicleService;
import com.idc.idc.service.impl.TaskServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SimulationJob {

    private TaskService taskService;
    private VehicleService vehicleService;

    @Autowired
    public SimulationJob(TaskService taskService,
                         VehicleService vehicleService) {
        this.taskService = taskService;
        this.vehicleService = vehicleService;
    }

    @Scheduled(fixedDelayString = "2000")
    public void updateLocation() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        for (Vehicle vehicle : vehicles) {
            log.info("Move vehicle {}", vehicle.getId());
            taskService.updatePositionOfVehicle(vehicle.getId());
        }
    }
}
