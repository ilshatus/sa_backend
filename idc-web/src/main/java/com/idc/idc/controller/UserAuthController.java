package com.idc.idc.controller;

import com.idc.idc.CurrentUser;
import com.idc.idc.dto.form.UserRegistrationForm;
import com.idc.idc.dto.json.RegistrationJson;
import com.idc.idc.dto.json.TokenJson;
import com.idc.idc.dto.json.ValidationErrorJson;
import com.idc.idc.response.HttpResponseStatus;
import com.idc.idc.response.Response;
import com.idc.idc.service.AuthTokenService;
import com.idc.idc.service.UserService;
import com.idc.idc.util.ValidationUtils;
import com.idc.idc.validator.UserRegistrationFormValidator;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.validator.routines.EmailValidator;


import javax.validation.Valid;
import java.util.List;


@Api(tags = {"Customer.Auth"})
@RestController
@RequestMapping(UserAuthController.ROOT_URL)
public class UserAuthController {
    public static final String ROOT_URL = "/v1/users";
    public static final String REGISTER_URL = "/register";
    public static final String LOGIN_URL = "/login";

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthController.class);

    private final AuthTokenService authTokenService;
    private final UserService userService;
    private final UserRegistrationFormValidator userRegistrationFormValidator;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserAuthController(AuthTokenService authTokenService,
                              UserService userService,
                              UserRegistrationFormValidator userRegistrationFormValidator,
                              AuthenticationManager authenticationManager) {
        this.authTokenService = authTokenService;
        this.userService = userService;
        this.userRegistrationFormValidator = userRegistrationFormValidator;
        this.authenticationManager = authenticationManager;
    }

    @InitBinder("userRegistrationForm")
    public void initBinderUserRegistrationForm(WebDataBinder binder) {
        binder.addValidators(userRegistrationFormValidator);
    }

    @ApiOperation(value = "Sign up by email, name and password")
    @PostMapping(REGISTER_URL)
    public ResponseEntity<Response<RegistrationJson>> signUp(
            @Valid @ModelAttribute UserRegistrationForm userRegistrationForm, BindingResult bindingResult) {

        String email = StringUtils.trim(userRegistrationForm.getEmail());
        String password = StringUtils.trim(userRegistrationForm.getPassword());
        LOGGER.debug("Signing up with params account [{}]", email);

        RegistrationJson registrationJson = new RegistrationJson();

        if (bindingResult.hasErrors()) {
            List<ValidationErrorJson> validationErrors = ValidationUtils.getValidationErrors(bindingResult);
            registrationJson.setErrors(validationErrors);
            return new ResponseEntity<>(
                    new Response<>(registrationJson, HttpResponseStatus.VALIDATION_ERROR), HttpStatus.BAD_REQUEST);
        }
        try {
            userService.registerCustomer(userRegistrationForm);
            TokenJson tokenJson = authenticate(email, password, true);
            registrationJson.setSuccess(true);
            registrationJson.setToken(tokenJson);
            LOGGER.info("Successfully registered user with account [{}]", userRegistrationForm.getEmail());
            return new ResponseEntity<>(new Response<>(registrationJson), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Sign in by email and password")
    @ApiResponses({@ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = "Account or password not specified: \"" + HttpResponseStatus.EMPTY_PARAM + "\"; account param has invalid email: \"" + HttpResponseStatus.INVALID_PARAM
            + "\""), @ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = "Authentication failed")})
    @PostMapping(LOGIN_URL)
    public ResponseEntity<Response<TokenJson>> signIn(
            @ApiParam("Email") @RequestParam(value = "email", required = false) String email,
            @ApiParam("Password") @RequestParam(value = "password", required = false) String password) {
        email = StringUtils.trim(email);
        password = StringUtils.trim(password);

        LOGGER.debug("Signing in with params account [{}]", email);
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            return new ResponseEntity<>(new Response<>(null, HttpResponseStatus.EMPTY_PARAM), HttpStatus.BAD_REQUEST);
        }
        if (!EmailValidator.getInstance().isValid(email)) {
            LOGGER.info("Sign in failed: invalid account [{}]", email);
            return new ResponseEntity<>(new Response<>(null, HttpResponseStatus.INVALID_PARAM), HttpStatus.BAD_REQUEST);
        }
        try {
            TokenJson authentication = authenticate(email, password, false);
            return new ResponseEntity<>(new Response<>(authentication), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }


    private TokenJson authenticate(String email, String password, Boolean alreadyRegisteredAccount) {
        Long userId = null;
        TokenJson tokenJson = new TokenJson();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        if (authentication == null || !authentication.isAuthenticated()) {
            LOGGER.info("Failed to authenticate user [{}]", email);
            throw new BadCredentialsException("bad-credentials");
        }
        Object userDetailsObject = authentication.getDetails();
        if (userDetailsObject instanceof CurrentUser) {
            userId = ((CurrentUser) userDetailsObject).getId();
        }
        LOGGER.info("Successfully signed in user [{}] with account [{}]", userId, email);
        tokenJson.setUserId(userId);
        tokenJson.setToken(authTokenService.generateToken(userId));
        return tokenJson;
    }
}
