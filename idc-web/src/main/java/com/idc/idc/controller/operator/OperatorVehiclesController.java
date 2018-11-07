package com.idc.idc.controller.operator;

import com.idc.idc.dto.form.CreateVehicleForm;
import com.idc.idc.dto.json.VehicleJson;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.Task;
import com.idc.idc.model.Vehicle;
import com.idc.idc.model.enums.TaskStatus;
import com.idc.idc.response.Response;
import com.idc.idc.service.TaskService;
import com.idc.idc.service.VehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"Operator.Vehicles"})
@RestController
@RequestMapping(OperatorVehiclesController.ROOT_URL)
@Slf4j
public class OperatorVehiclesController {
    public static final String ROOT_URL = "/v1/operator/vehicles";
    public static final String VEHICLE_URL = "/{vehicle_id}";
    public static final String ADD_DRIVER_URL = VEHICLE_URL + "/driver";

    private VehicleService vehicleService;
    private TaskService taskService;

    @Autowired
    public OperatorVehiclesController(VehicleService vehicleService,
                                      TaskService taskService) {
        this.vehicleService = vehicleService;
        this.taskService = taskService;
    }

    @ApiOperation("Get all vehicles")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping
    public ResponseEntity<Response<List<VehicleJson>>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        List<VehicleJson> vehicleJsons = vehicles.stream()
                .map(vehicle -> {
                    Task task = null;
                    List<Task> tasks = taskService.getTasksByVehicleAndStatus(vehicle, TaskStatus.IN_PROGRESS);
                    if (!tasks.isEmpty()) task = tasks.get(0);
                    return VehicleJson.mapFromVehicle(vehicle, task);
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(new Response<>(vehicleJsons), HttpStatus.OK);
    }

    @ApiOperation("Get vehicle by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping(VEHICLE_URL)
    public ResponseEntity<Response<VehicleJson>> getVehicle(@PathVariable("vehicle_id") Long vehicleId) {
        try {
            Vehicle vehicle = vehicleService.getVehicle(vehicleId);
            Task task = null;
            List<Task> tasks = taskService.getTasksByVehicleAndStatus(vehicle, TaskStatus.IN_PROGRESS);
            if (!tasks.isEmpty()) task = tasks.get(0);
            return new ResponseEntity<>(new Response<>(VehicleJson.mapFromVehicle(vehicle, task)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation("Create vehicle")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping
    public ResponseEntity<Response<VehicleJson>> createVehicle(@Valid @RequestBody CreateVehicleForm form,
                                                               BindingResult errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(
                    new Response<>(null, errors), HttpStatus.BAD_REQUEST);
        }
        try {
            Vehicle vehicle = vehicleService.createVehicle(form);
            return new ResponseEntity<>(new Response<>(VehicleJson.mapFromVehicle(vehicle)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation("Add driver to vehicle")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping(ADD_DRIVER_URL)
    public ResponseEntity<Response<VehicleJson>> addDriver(@PathVariable("vehicle_id") Long vehicleId,
                                                           @RequestBody Long driverId) {
        if (driverId == null) {
            return new ResponseEntity<>(new Response<>(null,
                    "Driver id must not be empty"), HttpStatus.BAD_REQUEST);
        }
        try {
            Vehicle vehicle = vehicleService.addDriver(vehicleId, driverId);
            return new ResponseEntity<>(new Response<>(VehicleJson.mapFromVehicle(vehicle)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
