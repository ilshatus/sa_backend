package com.idc.idc;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
public class RoleUsernamePasswordAuthToken extends UsernamePasswordAuthenticationToken {
    private UserType userType;

    public RoleUsernamePasswordAuthToken(Object principal, Object credentials, UserType userType) {
        super(principal, credentials);
        this.userType = userType;
    }
}
