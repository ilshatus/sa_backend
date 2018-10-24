package com.idc.idc.controller.operator;

import com.idc.idc.UserType;
import com.idc.idc.dto.form.SignInForm;
import com.idc.idc.dto.json.TokenJson;
import com.idc.idc.response.Response;
import com.idc.idc.util.Authenticator;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"Operator.Auth"})
@RestController
@RequestMapping(OperatorAuthController.ROOT_URL)
@Slf4j
public class OperatorAuthController {
    public static final String ROOT_URL = "/v1/operator";
    public static final String LOGIN_URL = "/login";

    private final Authenticator authenticator;

    public OperatorAuthController(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @ApiOperation(value = "Sign in by email and password")
    @ApiResponses({@ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = "Authentication failed"),
            @ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = "Empty credentials")})
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
            TokenJson authentication = authenticator.authenticate(email, password, UserType.OPERATOR);
            return new ResponseEntity<>(new Response<>(authentication), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}
