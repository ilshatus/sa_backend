package com.idc.idc.util;

import com.idc.idc.CurrentUser;
import com.idc.idc.RoleUsernamePasswordAuthToken;
import com.idc.idc.User;
import com.idc.idc.UserType;
import com.idc.idc.dto.json.TokenJson;
import com.idc.idc.service.AuthTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Authenticator {
    private final AuthenticationManager authenticationManager;
    private final AuthTokenService authTokenService;

    @Autowired
    public Authenticator(AuthenticationManager authenticationManager,
                         AuthTokenService authTokenService) {
        this.authenticationManager = authenticationManager;
        this.authTokenService = authTokenService;
    }

    public TokenJson authenticate(String email, String password, UserType userType) {
        TokenJson tokenJson = new TokenJson();
        RoleUsernamePasswordAuthToken authenticationToken =
                new RoleUsernamePasswordAuthToken(email, password, userType);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        if (authentication == null || !authentication.isAuthenticated()) {
            log.info("Failed to authenticate {} [{}]", userType.name(), email);
            throw new BadCredentialsException("bad-credentials");
        }

        Long userId = null;
        Object userDetailsObject = authentication.getDetails();
        if (userDetailsObject instanceof CurrentUser) {
            userId = ((CurrentUser) userDetailsObject).getId();
        }
        log.info("Successfully signed in user [{}] with account [{}]", userId, email);
        tokenJson.setUserId(userId);
        tokenJson.setToken(authTokenService.generateToken(new User(userId, userType)));
        return tokenJson;
    }
}
