package com.idc.idc.controller.customer;

import com.idc.idc.CurrentUser;
import com.idc.idc.dto.form.OrderCreationForm;
import com.idc.idc.dto.json.OrderJson;
import com.idc.idc.model.Order;
import com.idc.idc.response.Response;
import com.idc.idc.service.OrderService;
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

    private final OrderService orderService;

    @Autowired
    public CustomerOrdersController(OrderService orderService) {
        this.orderService = orderService;
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
        List<OrderJson> orderJsons = orders.stream().map(OrderJson::mapFromOrder).collect(Collectors.toList());
        return new ResponseEntity<>(new Response<>(orderJsons), HttpStatus.OK);
    }
}
