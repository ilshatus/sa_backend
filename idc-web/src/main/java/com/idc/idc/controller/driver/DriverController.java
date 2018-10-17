package com.idc.idc.controller.driver;

import com.idc.idc.dto.json.CustomerInfoJson;
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

@Api(tags = {"Drivers"})
@RestController
@RequestMapping(DriverController.ROOT_URL)
@Slf4j
public class DriverController {
    static final String ROOT_URL = "/v1/driver";
    static final String ORDER_URL = ROOT_URL + "/order";

    private OrderService orderService;

    @Autowired
    public DriverController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation(value = "Get customer info")
    @GetMapping(ORDER_URL + "/{order_id}")
    public ResponseEntity<Response<CustomerInfoJson>> vehicleInformation(
            @PathVariable("order_id") Long orderId
    ) {
        if (orderId == null) {
            return null;
        }
        try {
            Order order = orderService.getOrder(orderId);
            CustomerInfoJson infoJson = CustomerInfoJson.mapFromCustomer(order.getCustomer());
            return new ResponseEntity<>(new Response<>(infoJson), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
