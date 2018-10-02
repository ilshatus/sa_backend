package com.idc.idc;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomSecurityContext implements SecurityContext {
    private static final long serialVersionUID = -5159263748004567722L;

    private UserDetails userDetails;

    public CustomSecurityContext(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public Authentication getAuthentication() {
        return new AuthenticationImpl(userDetails);
    }

    @Override
    public void setAuthentication(Authentication arg0) {

    }
}
