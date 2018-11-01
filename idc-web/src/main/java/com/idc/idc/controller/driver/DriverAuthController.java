package com.idc.idc.controller.driver;

import com.idc.idc.UserType;
import com.idc.idc.dto.form.SignInForm;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"Driver.Auth"})
@RestController
@RequestMapping(DriverAuthController.ROOT_URL)
@Slf4j
public class DriverAuthController {
    public static final String ROOT_URL = "/v1/driver";
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";

    private final Authenticator authenticator;
    private UserService userService;


    @Autowired
    public DriverAuthController(Authenticator authenticator,
                                UserService userService) {
        this.authenticator = authenticator;
        this.userService = userService;
    }

    @ApiOperation(value = "Sign in by email and password")
    @ApiResponses({@ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = "Authentication failed")})
    @PostMapping(LOGIN_URL)
    public ResponseEntity<Response<TokenJson>> signIn(@RequestBody SignInForm form,
                                                      BindingResult errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(
                    new Response<>(null, errors), HttpStatus.BAD_REQUEST);
        }
        String email = StringUtils.trim(form.getEmail()).toLowerCase();
        String password = form.getPassword();
        String token = form.getFirebaseToken();
        if (token == null){
            return new ResponseEntity<>(
                    new Response<>(null, "Firebase tokem mustn't be empty"), HttpStatus.BAD_REQUEST);
        }
        log.debug("Signing in with params account [{}]", email);
        try {
            TokenJson authentication = authenticator.authenticate(email, password, UserType.DRIVER);
            userService.setFirebaseTokenToDriver(authentication.getUserId(), token);
            return new ResponseEntity<>(new Response<>(authentication), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "Log out")
    @PostMapping(LOGOUT_URL)
    public ResponseEntity<Response<String>> logout(@AuthenticationPrincipal Long driverId) {
        userService.setFirebaseTokenToDriver(driverId, "");
        return new ResponseEntity<>(new Response<>("success"), HttpStatus.OK);
    }
}
