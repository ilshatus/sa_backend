package com.idc.idc.controller.customer;

import com.idc.idc.CurrentUser;
import com.idc.idc.dto.form.OrderCreationForm;
import com.idc.idc.dto.json.OrderJson;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.exception.UnauthorizedException;
import com.idc.idc.model.Order;
import com.idc.idc.model.Task;
import com.idc.idc.model.enums.TaskStatus;
import com.idc.idc.response.Response;
import com.idc.idc.service.OrderService;
import com.idc.idc.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"Customer.Orders"})
@RestController
@RequestMapping(CustomerOrdersController.ROOT_URL)
@Slf4j
public class CustomerOrdersController {
    public static final String ROOT_URL = "/v1/customer/orders";
    public static final String ORDER_URL = "/{order_id}";


    private final OrderService orderService;
    private final TaskService taskService;

    @Autowired
    public CustomerOrdersController(OrderService orderService,
                                    TaskService taskService) {
        this.orderService = orderService;
        this.taskService = taskService;
    }

    @ApiOperation(value = "Create order")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping
    public ResponseEntity<Response<OrderJson>> createOrder(@AuthenticationPrincipal CurrentUser currentUser,
                                                           @Valid @RequestBody OrderCreationForm orderCreationForm,
                                                           BindingResult errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(
                    new Response<>(null, errors), HttpStatus.BAD_REQUEST);
        }
        Order order = orderService.createOrder(orderCreationForm, currentUser.getId());
        return new ResponseEntity<>(new Response<>(OrderJson.mapFromOrder(order)), HttpStatus.OK);
    }

    @ApiOperation("Get all orders of customer by limit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping
    public ResponseEntity<Response<List<OrderJson>>> getAllOrders(@RequestParam(required = false, defaultValue = "10") Integer limit,
                                                                  @RequestParam(required = false, defaultValue = "0") Integer offset,
                                                                  @AuthenticationPrincipal CurrentUser user) {
        List<Order> orders = orderService.getOrdersOfCustomer(user.getId(), limit, offset);
        List<OrderJson> orderJsons = orders.stream().map(order -> {
            OrderJson orderJson = OrderJson.mapFromOrder(order);
            try {
                Task task = taskService.getTaskByOrderAndStatus(order, TaskStatus.IN_PROGRESS);
                orderJson.setRouteId(task.getRouteId());
            } catch (NotFoundException ee) {

            }
            return orderJson;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(new Response<>(orderJsons), HttpStatus.OK);
    }

    @ApiOperation("Get order of customer")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping(ORDER_URL)
    public ResponseEntity<Response<OrderJson>> getOrder(@PathVariable("order_id") Long orderId,
                                                        @AuthenticationPrincipal CurrentUser user) {
        try {
            Order order = orderService.getOrder(orderId);
            if (!order.getCustomer().getId().equals(user.getId()))
                throw new UnauthorizedException(
                        String.format("Order %d not belong to customer %d", orderId, user.getId()));
            OrderJson orderJson = OrderJson.mapFromOrder(order);
            try {
                Task task = taskService.getTaskByOrderAndStatus(order, TaskStatus.IN_PROGRESS);
                orderJson.setRouteId(task.getRouteId());
            } catch (NotFoundException ee) {

            }
            return new ResponseEntity<>(new Response<>(orderJson), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }
}
