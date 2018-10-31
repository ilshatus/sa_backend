package com.idc.idc.controller.operator;

import com.idc.idc.dto.json.DriverJson;
import com.idc.idc.dto.json.VehicleJson;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.Vehicle;
import com.idc.idc.model.users.Driver;
import com.idc.idc.response.Response;
import com.idc.idc.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"Operator.Drivers"})
@RestController
@RequestMapping(OperatorDriversController.ROOT_URL)
@Slf4j
public class OperatorDriversController {
    public static final String ROOT_URL = "/v1/operator/drivers";
    public static final String DRIVER_URL = "/{driver_id}";

    private UserService userService;

    public OperatorDriversController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("Get all drivers")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping
    public ResponseEntity<Response<List<DriverJson>>> getAllDrivers() {
        List<Driver> drivers = userService.getAllDrivers();
        List<DriverJson> driverJsons = drivers.stream().map(DriverJson::mapFromDriver).collect(Collectors.toList());
        return new ResponseEntity<>(new Response<>(driverJsons), HttpStatus.OK);
    }

    @ApiOperation("Get driver by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping(DRIVER_URL)
    public ResponseEntity<Response<DriverJson>> getDriver(@PathVariable("driver_id") Long driverId) {
        try {
            Driver driver = userService.getDriver(driverId);
            return new ResponseEntity<>(new Response<>(DriverJson.mapFromDriver(driver)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
