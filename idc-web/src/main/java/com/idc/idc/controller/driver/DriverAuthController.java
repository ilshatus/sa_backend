package com.idc.idc.controller.driver;

import com.idc.idc.UserType;
import com.idc.idc.dto.json.TokenJson;
import com.idc.idc.response.Response;
import com.idc.idc.service.UserService;
import com.idc.idc.util.Authenticator;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Driver.Auth"})
@RestController
@RequestMapping(DriverAuthController.ROOT_URL)
@Slf4j
public class DriverAuthController {
    public static final String ROOT_URL = "/v1/drivers";
    public static final String LOGIN_URL = "/login";

    private final Authenticator authenticator;

    public DriverAuthController(Authenticator authenticator) {
        this.authenticator = authenticator;
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
            TokenJson authentication = authenticator.authenticate(email, password, UserType.DRIVER);
            return new ResponseEntity<>(new Response<>(authentication), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}
