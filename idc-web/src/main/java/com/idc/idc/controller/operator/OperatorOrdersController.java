package com.idc.idc.controller.operator;

import com.idc.idc.CurrentUser;
import com.idc.idc.dto.json.OrderJson;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.Order;
import com.idc.idc.response.Response;
import com.idc.idc.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"Operator.Orders"})
@RestController
@RequestMapping(OperatorOrdersController.ROOT_URL)
@Slf4j
public class OperatorOrdersController {
    public static final String ROOT_URL = "/v1/operator/orders";
    public static final String ORDER = "/{order_id}";

    private OrderService orderService;

    public OperatorOrdersController(OrderService orderService) {
        this.orderService = orderService;
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
    @GetMapping(ORDER)
    public ResponseEntity<Response<OrderJson>> getOrder(@PathVariable("order_id") Long orderId) {
        try {
            Order order = orderService.getOrder(orderId);
            return new ResponseEntity<>(new Response<>(OrderJson.mapFromOrder(order)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
