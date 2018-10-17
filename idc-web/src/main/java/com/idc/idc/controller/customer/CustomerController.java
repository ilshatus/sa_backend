package com.idc.idc.controller.customer;

import com.idc.idc.CurrentUser;
import com.idc.idc.dto.form.OrderCreationForm;
import com.idc.idc.dto.json.OrderJson;
import com.idc.idc.dto.json.OrderSuccessJson;
import com.idc.idc.model.Order;
import com.idc.idc.model.users.Customer;
import com.idc.idc.response.Response;
import com.idc.idc.service.OrderService;
import com.idc.idc.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"Customer"})
@RestController
@RequestMapping(CustomerController.ROOT_URL)
@Slf4j
public class CustomerController {
    public static final String ROOT_URL = "/v1/customer";
    public static final String ORDER = "/order";

    private final OrderService orderService;

    @Autowired
    public CustomerController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation(value = "Create order")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping(ORDER)
    public ResponseEntity<Response<OrderJson>> createOrder(@AuthenticationPrincipal CurrentUser currentUser,
                                                           @ModelAttribute OrderCreationForm orderCreationForm,
                                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Response<>(null, "validation-error"), HttpStatus.BAD_REQUEST);
        }
        Order order = orderService.createOrder(orderCreationForm, currentUser.getId());
        return new ResponseEntity<>(new Response<>(OrderJson.mapFromOrder(order)), HttpStatus.OK);
    }

}
