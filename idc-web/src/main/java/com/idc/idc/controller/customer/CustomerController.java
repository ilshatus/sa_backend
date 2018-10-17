package com.idc.idc.controller.customer;

import com.idc.idc.CurrentUser;
import com.idc.idc.dto.form.OrderCreationForm;
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
import org.springframework.web.bind.annotation.*;

@Api(tags = {"Customer"})
@RestController
@RequestMapping(CustomerController.ROOT_URL)
@Slf4j
public class CustomerController {
    public static final String ROOT_URL = "/v1/customer";
    public static final String ORDER = "/order";

    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    public CustomerController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @ApiOperation(value = "Create order")
    @PostMapping(ORDER)
    public ResponseEntity<Response<OrderSuccessJson>> createOrder(@AuthenticationPrincipal CurrentUser currentUser,
                                                                  @ModelAttribute OrderCreationForm orderCreationForm) {
        Long currentId = currentUser.getId();
        Customer currentCustomer = userService.getCustomer(currentId);
        Order order = orderCreationForm.toOrder(currentCustomer);
        orderService.submitOrder(order);
        OrderSuccessJson orderSuccessJson = new OrderSuccessJson();
        return new ResponseEntity<>(new Response<>(orderSuccessJson), HttpStatus.OK);
    }

}
