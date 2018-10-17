package com.idc.idc.controller.operator;

import com.idc.idc.dto.json.SimpleOperationStatusJson;
import com.idc.idc.dto.json.VehicleJson;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.Order;
import com.idc.idc.model.Task;
import com.idc.idc.model.Vehicle;
import com.idc.idc.model.enums.TaskStatus;
import com.idc.idc.response.Response;
import com.idc.idc.service.OrderService;
import com.idc.idc.service.TaskService;
import com.idc.idc.service.VehicleService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.jclouds.oauth.v2.config.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"Operators"})
@RestController
@RequestMapping(OperatorController.ROOT_URL)
@Slf4j
public class OperatorController {
    static final String ROOT_URL = "/v1/operator";
    static final String VEHICLE_URL = ROOT_URL + "/vehicle";

    private OrderService orderService;
    private VehicleService vehicleService;
    private TaskService taskService;

    @Autowired
    public OperatorController(OrderService orderService,
                              VehicleService vehicleService,
                              TaskService taskService) {
        this.orderService = orderService;
        this.vehicleService = vehicleService;
        this.taskService = taskService;
    }

    @ApiOperation(value = "Get information about driver")
    @GetMapping(VEHICLE_URL + "/{vehicle_id}")
    public ResponseEntity<Response<VehicleJson>> vehicleInformation(
            @PathVariable("vehicle_id") Long vehicleId
    ) {
        if (vehicleId == null) {
            return null;
        }
        try {
            Vehicle vehicle = vehicleService.getVehicle(vehicleId);
            return new ResponseEntity<>(new Response<>(VehicleJson.mapFromVehicle(vehicle)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Assign delivery to the driver")
    @PostMapping(VEHICLE_URL)
    public ResponseEntity<Response<SimpleOperationStatusJson>> assignTask(
            @RequestParam("vehicle_id") Long vehicleId,
            @RequestParam("order_id") Long orderId
    ) {
        SimpleOperationStatusJson simpleOperationStatusJson = new SimpleOperationStatusJson();
        if (vehicleId == null | orderId == null) {
            return new ResponseEntity<>(new Response<>(simpleOperationStatusJson), HttpStatus.OK);
        }
        try {
            simpleOperationStatusJson = new SimpleOperationStatusJson();
            Vehicle vehicle = vehicleService.getVehicle(vehicleId);
            Order order = orderService.getOrder(orderId);
            Task task = Task.builder()
                    .order(order)
                    .vehicle(vehicle)
                    .status(TaskStatus.PENDING)
                    .build();
            taskService.submitTask(task);
            simpleOperationStatusJson.setSuccess(true);
            return new ResponseEntity<>(new Response<>(simpleOperationStatusJson), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
