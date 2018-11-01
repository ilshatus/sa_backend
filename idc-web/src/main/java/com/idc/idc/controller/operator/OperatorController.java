package com.idc.idc.controller.operator;

import com.idc.idc.CurrentUser;
import com.idc.idc.dto.json.OperatorJson;
import com.idc.idc.model.users.Operator;
import com.idc.idc.response.Response;
import com.idc.idc.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Operator"})
@RestController
@RequestMapping(OperatorController.ROOT_URL)
@Slf4j
public class OperatorController {
    public static final String ROOT_URL = "/v1/operator";

    private UserService userService;

    @Autowired
    public OperatorController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("Get operator's profile")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping
    public ResponseEntity<Response<OperatorJson>> getProfile(@AuthenticationPrincipal CurrentUser currentUser) {
        Operator operator = userService.getOperator(currentUser.getId());
        return new ResponseEntity<>(new Response<>(OperatorJson.mapFromOperator(operator)), HttpStatus.OK);
    }
}
