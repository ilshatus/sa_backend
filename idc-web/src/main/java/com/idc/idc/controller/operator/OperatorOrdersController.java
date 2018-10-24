package com.idc.idc.controller.operator;

import com.idc.idc.dto.form.CreateTaskForm;
import com.idc.idc.dto.json.OrderJson;
import com.idc.idc.dto.json.TaskJson;
import com.idc.idc.dto.json.VehicleJson;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.Order;
import com.idc.idc.model.Task;
import com.idc.idc.model.Vehicle;
import com.idc.idc.response.Response;
import com.idc.idc.service.OrderService;
import com.idc.idc.service.TaskService;
import com.idc.idc.service.VehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"Operator.Orders"})
@RestController
@RequestMapping(OperatorOrdersController.ROOT_URL)
@Slf4j
public class OperatorOrdersController {
    public static final String ROOT_URL = "/v1/operator/orders";
    public static final String ORDER_URL = "/{order_id}";
    public static final String CONFIRM_URL = ORDER_URL + "/confirm";
    public static final String NEAREST_VEHICLES = ORDER_URL + "/nearest/vehicles";

    private OrderService orderService;
    private VehicleService vehicleService;
    private TaskService taskService;

    public OperatorOrdersController(OrderService orderService,
                                    VehicleService vehicleService,
                                    TaskService taskService) {
        this.orderService = orderService;
        this.vehicleService = vehicleService;
        this.taskService = taskService;
    }

    @ApiOperation("Get all orders by limit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping
    public ResponseEntity<Response<List<OrderJson>>> getAllOrders(@RequestParam Integer limit,
                                                                  @RequestParam Integer offset) {
        List<Order> orders = orderService.getAllOrders(limit, offset);
        List<OrderJson> orderJsons = orders.stream().map(OrderJson::mapFromOrder).collect(Collectors.toList());
        return new ResponseEntity<>(new Response<>(orderJsons), HttpStatus.OK);
    }

    @ApiOperation("Get order by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping(ORDER_URL)
    public ResponseEntity<Response<OrderJson>> getOrder(@PathVariable("order_id") Long orderId) {
        try {
            Order order = orderService.getOrder(orderId);
            return new ResponseEntity<>(new Response<>(OrderJson.mapFromOrder(order)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation("Get nearest vehicles to given order")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping(NEAREST_VEHICLES)
    public ResponseEntity<Response<List<VehicleJson>>> getNearestVehicles(@PathVariable("order_id") Long orderId,
                                                                          @RequestParam Integer limit) {
        try {
            List<Vehicle> vehicles = vehicleService.getNearestVehicles(orderId, limit);
            List<VehicleJson> vehicleJsons = vehicles.stream()
                    .map(VehicleJson::mapFromVehicle)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(new Response<>(vehicleJsons), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation("Confirm order by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping(CONFIRM_URL)
    public ResponseEntity<Response<TaskJson>> confirmOrder(@PathVariable("order_id") Long orderId,
                                                           @Valid @RequestBody CreateTaskForm form,
                                                           BindingResult errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(
                    new Response<>(null, errors), HttpStatus.BAD_REQUEST);
        }

        try {
            Task task = taskService.createTask(orderId, form);
            return new ResponseEntity<>(new Response<>(TaskJson.mapFromTask(task)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
