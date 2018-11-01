package com.idc.idc.controller.customer;

import com.idc.idc.UserType;
import com.idc.idc.dto.form.SignInForm;
import com.idc.idc.dto.form.UserRegistrationForm;
import com.idc.idc.dto.json.TokenJson;
import com.idc.idc.response.Response;
import com.idc.idc.service.UserService;
import com.idc.idc.util.Authenticator;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"Customer.Auth"})
@RestController
@RequestMapping(CustomerAuthController.ROOT_URL)
@Slf4j
public class CustomerAuthController {
    public static final String ROOT_URL = "/v1/customer";
    public static final String REGISTER_URL = "/register";
    public static final String LOGIN_URL = "/login";

    private final UserService userService;
    private final Authenticator authenticator;

    @Autowired
    public CustomerAuthController(UserService userService,
                                  Authenticator authenticator) {
        this.userService = userService;
        this.authenticator = authenticator;
    }

    @ApiOperation(value = "Sign up by email, name and password")
    @ApiResponses({ @ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = "Registration failed") })
    @PostMapping(REGISTER_URL)
    public ResponseEntity<Response<TokenJson>> signUp(
            @Valid @RequestBody UserRegistrationForm userRegistrationForm,
            BindingResult errors) {

        if (errors.hasErrors()) {
            return new ResponseEntity<>(
                    new Response<>(null, errors), HttpStatus.BAD_REQUEST);
        }

        String email = StringUtils.trim(userRegistrationForm.getEmail()).toLowerCase();
        String password = userRegistrationForm.getPassword();
        log.debug("Signing up with params account [{}]", email);

        try {
            userService.registerCustomer(userRegistrationForm);
            TokenJson tokenJson = authenticator.authenticate(email, password ,UserType.CUSTOMER);
            log.info("Successfully registered user with account [{}]", userRegistrationForm.getEmail());
            return new ResponseEntity<>(new Response<>(tokenJson), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Sign in by email and password")
    @ApiResponses({ @ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = "Authentication failed") })
    @PostMapping(LOGIN_URL)
    public ResponseEntity<Response<TokenJson>> signIn(@RequestBody SignInForm form,
                                                      BindingResult errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(
                    new Response<>(null, errors), HttpStatus.BAD_REQUEST);
        }
        String email = StringUtils.trim(form.getEmail()).toLowerCase();
        String password = form.getPassword();

        log.debug("Signing in with params account [{}]", email);
        try {
            TokenJson authentication = authenticator.authenticate(email, password, UserType.CUSTOMER);
            return new ResponseEntity<>(new Response<>(authentication), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}
