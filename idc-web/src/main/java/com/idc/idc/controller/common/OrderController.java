package com.idc.idc.controller.common;

import com.idc.idc.dto.json.SimpleOrderJson;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.Order;
import com.idc.idc.response.Response;
import com.idc.idc.service.OrderService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"Common.Orders"})
@RestController
@RequestMapping(OrderController.ROOT_URL)
@Slf4j
public class OrderController {
    public static final String ROOT_URL = "/v1/orders";
    public static final String TRACK_URL = ROOT_URL + "/track/{trackingCode}";

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation(value = "Get location of order")
    @ApiResponses({ @ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = "Order not found") })
    @GetMapping(TRACK_URL)
    public ResponseEntity<Response<SimpleOrderJson>> getLocation(
            @ApiParam("Tracking_Code") @PathVariable("trackingCode") String trackingCode) {
        try {
            Order order = orderService.getOrder(trackingCode);
            SimpleOrderJson simpleOrderJson = SimpleOrderJson.mapFromOrder(order);
            return new ResponseEntity<>(new Response<>(simpleOrderJson), HttpStatus.OK);
        } catch (NotFoundException e) {
            log.debug("Order with tracking code [{}] wasn't found", trackingCode);
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
