package com.idc.idc.controller.operator;


import com.idc.idc.CurrentUser;
import com.idc.idc.dto.json.VehicleJson;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.Vehicle;
import com.idc.idc.response.Response;
import com.idc.idc.service.VehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"Operator.Vehicle"})
@RestController
@RequestMapping(OperatorVehicleController.ROOT_URL)
@Slf4j
public class OperatorVehicleController {
    public static final String ROOT_URL = "/v1/operator/vehicle";
    public static final String VEHICLE = "/{vehicle_id}";

    private VehicleService vehicleService;

    public OperatorVehicleController(VehicleService vehicleeService) {
        this.vehicleService = vehicleService;
    }

    @ApiOperation("Get all Vehicles")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping
    public ResponseEntity<Response<List<VehicleJson>>> getAllVehicles(@RequestParam Integer limit,
                                                                    @RequestParam Integer offset,
                                                                    @AuthenticationPrincipal CurrentUser currentUser) {
        List<Vehicle> vehicles = vehicleService.getAllVehicles(limit, offset);
        List<VehicleJson> vehicleJsons = vehicles.stream().map(VehicleJson::mapFromVehicle).collect(Collectors.toList());
        return new ResponseEntity<>(new Response<>(vehicleJsons), HttpStatus.OK);
    }

    @ApiOperation("Get vehicle by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping(VEHICLE)
    public ResponseEntity<Response<VehicleJson>> getVehicle(@PathVariable("vehicle_id") Long vehicleId,
                                                        @AuthenticationPrincipal CurrentUser currentUser) {
        try {
            Vehicle vehicle = vehicleService.getVehicle(vehicleId);
            return new ResponseEntity<>(new Response<>(VehicleJson.mapFromVehicle(vehicle)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
