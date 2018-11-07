package com.idc.idc.controller.customer;

import com.idc.idc.CurrentUser;
import com.idc.idc.dto.json.CustomerJson;
import com.idc.idc.dto.json.OperatorJson;
import com.idc.idc.model.users.Customer;
import com.idc.idc.model.users.Operator;
import com.idc.idc.response.Response;
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

    private UserService userService;

    @Autowired
    public CustomerController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("Get customers's profile")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping
    public ResponseEntity<Response<CustomerJson>> getProfile(@AuthenticationPrincipal CurrentUser currentUser) {
        Customer customer = userService.getCustomer(currentUser.getId());
        return new ResponseEntity<>(new Response<>(CustomerJson.mapFromCustomer(customer)), HttpStatus.OK);
    }
}
