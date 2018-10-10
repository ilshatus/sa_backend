package com.idc.idc.controller.customer;

import com.idc.idc.UserType;
import com.idc.idc.dto.form.UserRegistrationForm;
import com.idc.idc.dto.json.CustomerRegistrationJson;
import com.idc.idc.dto.json.TokenJson;
import com.idc.idc.dto.json.ValidationErrorJson;
import com.idc.idc.response.HttpResponseStatus;
import com.idc.idc.response.Response;
import com.idc.idc.service.UserService;
import com.idc.idc.util.Authenticator;
import com.idc.idc.util.ValidationUtils;
import com.idc.idc.validator.UserRegistrationFormValidator;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"Customer.Auth"})
@RestController
@RequestMapping(CustomerAuthController.ROOT_URL)
@Slf4j
public class CustomerAuthController {
    public static final String ROOT_URL = "/v1/customers";
    public static final String REGISTER_URL = "/register";
    public static final String LOGIN_URL = "/login";

    private final UserService userService;
    private final UserRegistrationFormValidator userRegistrationFormValidator;
    private final Authenticator authenticator;

    @Autowired
    public CustomerAuthController(UserService userService,
                                  UserRegistrationFormValidator userRegistrationFormValidator,
                                  Authenticator authenticator) {
        this.userService = userService;
        this.userRegistrationFormValidator = userRegistrationFormValidator;
        this.authenticator = authenticator;
    }

    @InitBinder("userRegistrationForm")
    public void initBinderUserRegistrationForm(WebDataBinder binder) {
        binder.addValidators(userRegistrationFormValidator);
    }

    @ApiOperation(value = "Sign up by email, name and password")
    @PostMapping(REGISTER_URL)
    public ResponseEntity<Response<CustomerRegistrationJson>> signUp(
            @Valid @ModelAttribute UserRegistrationForm userRegistrationForm, BindingResult bindingResult) {

        String email = StringUtils.trim(userRegistrationForm.getEmail()).toLowerCase();
        String password = userRegistrationForm.getPassword();
        log.debug("Signing up with params account [{}]", email);

        CustomerRegistrationJson customerRegistrationJson = new CustomerRegistrationJson();

        if (bindingResult.hasErrors()) {
            List<ValidationErrorJson> validationErrors = ValidationUtils.getValidationErrors(bindingResult);
            customerRegistrationJson.setErrors(validationErrors);
            return new ResponseEntity<>(
                    new Response<>(customerRegistrationJson, HttpResponseStatus.VALIDATION_ERROR), HttpStatus.BAD_REQUEST);
        }
        try {
            userService.registerCustomer(userRegistrationForm);
            TokenJson tokenJson = authenticator.authenticate(email, password ,UserType.CUSTOMER);
            customerRegistrationJson.setSuccess(true);
            customerRegistrationJson.setToken(tokenJson);
            log.info("Successfully registered user with account [{}]", userRegistrationForm.getEmail());
            return new ResponseEntity<>(new Response<>(customerRegistrationJson), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Sign in by email and password")
    @ApiResponses({ @ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = "Authentication failed") })
    @PostMapping(LOGIN_URL)
    public ResponseEntity<Response<TokenJson>> signIn(
            @ApiParam("Email") @RequestParam(value = "email", required = false) String email,
            @ApiParam("Password") @RequestParam(value = "password", required = false) String password) {
        email = StringUtils.trim(email).toLowerCase();
        password = StringUtils.trim(password);

        log.debug("Signing in with params account [{}]", email);
        try {
            TokenJson authentication = authenticator.authenticate(email, password, UserType.CUSTOMER);
            return new ResponseEntity<>(new Response<>(authentication), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}
