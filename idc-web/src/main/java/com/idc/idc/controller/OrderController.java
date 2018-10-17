package com.idc.idc.controller;


import com.idc.idc.dto.json.TrackingInfoJson;
import com.idc.idc.model.Order;
import com.idc.idc.response.Response;
import com.idc.idc.service.OrderService;
import com.idc.idc.service.TrackingInformationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"TrackingInformation.Operations"})
@Slf4j
@RestController
@RequestMapping(OrderController.ROOT_URL)
public class OrderController {
    public static final String ROOT_URL = "/v1/orders";
    private static final String ORDERS = ROOT_URL + "/{order_id}";
    private static final String TRACK_INFO = ORDERS + "/trackinfo";

    private TrackingInformationService trackingInformationService;
    private OrderService orderService;
    @Autowired
    public OrderController(TrackingInformationService trackingInformationService,
                           OrderService orderService) {
        this.trackingInformationService = trackingInformationService;
        this.orderService = orderService;
    }

    @ApiOperation(value = "Get tracking information of the order")
    @GetMapping(TRACK_INFO)
    public ResponseEntity<Response<TrackingInfoJson>> getTrackingInformation(
            @ApiParam("Id of order") @PathVariable(value = "order_id", required = true) long id
    ) {
        log.debug("Getting order with id [{}] from db", id);
        Order order = orderService.getOrder(id);
        String trackingCode = order.getTracking_code();
        log.debug("Order with id [{}] was successfully got", id);
        try {
            log.debug("Getting tracking information of tracking code [{}]", trackingCode);
            TrackingInfoJson trackingInfoJson = TrackingInfoJson.mapFromTrackingInfo(
                    trackingInformationService.getTrackingInformation(trackingCode)
            );
            log.debug("Information of tracking code [{}] was successfully got", trackingCode);
            return new ResponseEntity<>(new Response<>(trackingInfoJson), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
