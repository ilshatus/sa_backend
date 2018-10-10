package com.idc.idc.controller.operator.admin;

import com.idc.idc.dto.form.UserRegistrationForm;
import com.idc.idc.dto.json.SimpleOperationStatusJson;
import com.idc.idc.dto.json.ValidationErrorJson;
import com.idc.idc.response.HttpResponseStatus;
import com.idc.idc.response.Response;
import com.idc.idc.service.UserService;
import com.idc.idc.util.ValidationUtils;
import com.idc.idc.validator.UserRegistrationFormValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"Admin.Personnel.Registration"})
@RestController
@RequestMapping(PersonnelRegistration.ROOT_URL)
@Slf4j
public class PersonnelRegistration {
    public static final String ROOT_URL = "/v1/admin/personnel";
    public static final String REGISTER_URL = "/register";
    public static final String REGISTER_OPERATOR = REGISTER_URL + "/operator";
    public static final String REGISTER_DRIVER = REGISTER_URL + "/driver";

    private final UserService userService;
    private final UserRegistrationFormValidator userRegistrationFormValidator;

    @Autowired
    public PersonnelRegistration(UserService userService,
                                  UserRegistrationFormValidator userRegistrationFormValidator) {
        this.userService = userService;
        this.userRegistrationFormValidator = userRegistrationFormValidator;
    }

    @InitBinder("userRegistrationForm")
    public void initBinderUserRegistrationForm(WebDataBinder binder) {
        binder.addValidators(userRegistrationFormValidator);
    }

    @ApiOperation(value = "Register operator by email, name and password")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping(REGISTER_OPERATOR)
    public ResponseEntity<Response<SimpleOperationStatusJson>> registerOperator(
            @Valid @ModelAttribute UserRegistrationForm userRegistrationForm, BindingResult bindingResult) {
        String email = StringUtils.trim(userRegistrationForm.getEmail()).toLowerCase();

        log.debug("Register operator with email [{}]", email);
        SimpleOperationStatusJson registrationJson = new SimpleOperationStatusJson();

        if (bindingResult.hasErrors()) {
            List<ValidationErrorJson> validationErrors = ValidationUtils.getValidationErrors(bindingResult);
            registrationJson.setErrors(validationErrors);
            return new ResponseEntity<>(
                    new Response<>(registrationJson, HttpResponseStatus.VALIDATION_ERROR), HttpStatus.BAD_REQUEST);
        }

        try {
            userService.registerOperator(userRegistrationForm);
            registrationJson.setSuccess(true);
            log.info("Successfully registered operator with email [{}]", userRegistrationForm.getEmail());
            return new ResponseEntity<>(new Response<>(registrationJson), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Register driver by email, name and password")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping(REGISTER_DRIVER)
    public ResponseEntity<Response<SimpleOperationStatusJson>> registerDriver(
            @Valid @ModelAttribute UserRegistrationForm userRegistrationForm, BindingResult bindingResult) {
        String email = StringUtils.trim(userRegistrationForm.getEmail()).toLowerCase();

        log.debug("Register driver with email [{}]", email);
        SimpleOperationStatusJson registrationJson = new SimpleOperationStatusJson();

        if (bindingResult.hasErrors()) {
            List<ValidationErrorJson> validationErrors = ValidationUtils.getValidationErrors(bindingResult);
            registrationJson.setErrors(validationErrors);
            return new ResponseEntity<>(
                    new Response<>(registrationJson, HttpResponseStatus.VALIDATION_ERROR), HttpStatus.BAD_REQUEST);
        }

        try {
            userService.registerDriver(userRegistrationForm);
            registrationJson.setSuccess(true);
            log.info("Successfully registered driver with email [{}]", userRegistrationForm.getEmail());
            return new ResponseEntity<>(new Response<>(registrationJson), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
