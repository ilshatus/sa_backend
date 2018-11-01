package com.idc.idc.controller.driver;

import com.idc.idc.CurrentUser;
import com.idc.idc.dto.form.LocationForm;
import com.idc.idc.dto.json.DriverJson;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.users.Driver;
import com.idc.idc.response.Response;
import com.idc.idc.service.UserService;
import com.idc.idc.service.VehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Api(tags = {"Driver"})
@RestController
@RequestMapping(DriverController.ROOT_URL)
@Slf4j
public class DriverController {
    public static final String ROOT_URL = "/v1/driver";
    public static final String LOCATION = "/location";

    private UserService userService;
    private VehicleService vehicleService;

    @Autowired
    public DriverController(UserService userService,
                            VehicleService vehicleService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
    }

    @ApiOperation("Get driver's profile")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping
    public ResponseEntity<Response<DriverJson>> getProfile(@AuthenticationPrincipal CurrentUser currentUser) {
        Driver driver = userService.getDriver(currentUser.getId());
        return new ResponseEntity<>(new Response<>(DriverJson.mapFromDriver(driver)), HttpStatus.OK);
    }

    @ApiOperation("Send location of current driver")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping(LOCATION)
    public ResponseEntity<Response<String>> sendLocation(@RequestBody LocationForm location,
                                                         @AuthenticationPrincipal CurrentUser currentUser,
                                                         BindingResult errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(
                    new Response<>(null, errors), HttpStatus.BAD_REQUEST);
        }
        try {
            vehicleService.updatePositionOfVehicle(currentUser.getId(), location.toCurrentLocation());
            return new ResponseEntity<>(new Response<>("ok"), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}

