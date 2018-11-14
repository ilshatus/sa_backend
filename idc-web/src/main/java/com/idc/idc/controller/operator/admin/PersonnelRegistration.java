package com.idc.idc.controller.operator.admin;

import com.idc.idc.dto.form.UserRegistrationForm;
import com.idc.idc.exception.RegistrationException;
import com.idc.idc.exception.StorageException;
import com.idc.idc.response.Response;
import com.idc.idc.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Api(tags = {"Admin.Personnel.Registration"})
@RestController
@RequestMapping(PersonnelRegistration.ROOT_URL)
@Slf4j
public class PersonnelRegistration {
    public static final String ROOT_URL = "/v1/admin/personnel";
    public static final String REGISTER_URL = "/register";
    public static final String REGISTER_OPERATOR = REGISTER_URL + "/operators";
    public static final String REGISTER_DRIVER = REGISTER_URL + "/drivers";

    private final UserService userService;

    @Autowired
    public PersonnelRegistration(UserService userService) {
        this.userService = userService;
    }


    @ApiOperation(value = "Register operator by email, name and password")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @ApiResponses({@ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = "Registration failed")})
    @PostMapping(REGISTER_OPERATOR)
    public ResponseEntity<Response<String>> registerOperator(
            @RequestParam("image_file") MultipartFile imageFile,
            @Valid @ModelAttribute UserRegistrationForm userRegistrationForm,
            BindingResult errors) {

        if (errors.hasErrors()) {
            return new ResponseEntity<>(
                    new Response<>(null, errors), HttpStatus.BAD_REQUEST);
        }

        String email = StringUtils.trim(userRegistrationForm.getEmail()).toLowerCase();
        log.debug("Register operator with email [{}]", email);

        try {
            userService.registerOperator(userRegistrationForm, getDataFromMultipartFile(imageFile));
            log.info("Successfully registered operator with email [{}]", userRegistrationForm.getEmail());
            return new ResponseEntity<>(new Response<>("success"), HttpStatus.OK);
        } catch (RegistrationException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Register driver by email, name and password")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @ApiResponses({@ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = "Registration failed")})
    @PostMapping(REGISTER_DRIVER)
    public ResponseEntity<Response<String>> registerDriver(
            @RequestParam("image_file") MultipartFile imageFile,
            @Valid @ModelAttribute UserRegistrationForm userRegistrationForm,
            BindingResult errors) {

        if (errors.hasErrors()) {
            return new ResponseEntity<>(
                    new Response<>(null, errors), HttpStatus.BAD_REQUEST);
        }

        String email = StringUtils.trim(userRegistrationForm.getEmail()).toLowerCase();

        log.debug("Register driver with email [{}]", email);


        try {
            userService.registerDriver(userRegistrationForm, getDataFromMultipartFile(imageFile));
            log.info("Successfully registered driver with email [{}]", userRegistrationForm.getEmail());
            return new ResponseEntity<>(new Response<>("success"), HttpStatus.OK);
        } catch (RegistrationException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    private byte[] getDataFromMultipartFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        byte[] data;
        try {
            data = file.getBytes();
        } catch (Exception e) {
            log.warn("Failed to get image from file", e);
            return null;
        }

        return data;
    }
}
