package com.idc.idc;

import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.users.Customer;
import com.idc.idc.service.UserService;
import com.idc.idc.service.AuthTokenService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Setter
@Getter
public class HeaderAuthenticationFilter extends FilterSecurityInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderAuthenticationFilter.class);

    private AuthTokenService authTokenService;
    private UserService userService;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        SecurityContext securityContext = loadSecurityContext(request);

        if (securityContext != null)
            SecurityContextHolder.setContext(securityContext);

        chain.doFilter(request, response);
    }

    private SecurityContext loadSecurityContext(HttpServletRequest request) {
        String xAuth = request.getHeader("Authorization");
        User user = null;

        try {
            user = authTokenService.getUserId(xAuth);
        } catch (IndexOutOfBoundsException e) {
            logger.info("Not valid token");
            return null;
        }

        try {
            switch (user.getUserType()) {
                case CUSTOMER:
                    userService.getCustomer(user.getId());
                    break;
                case DRIVER:
                    userService.getDriver(user.getId());
                    break;
                case OPERATOR:
                    userService.getOperator(user.getId());
            }
        } catch (NotFoundException e) {
            LOGGER.info("{} with id {} not found", user.getUserType().name(), user.getId());
            return null;
        }

        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList();
        authorityList.add(new SimpleGrantedAuthority(user.getUserType().name()));
        UserDetails userDetails = CurrentUser
                .builder()
                .id(user.getId())
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .authorities(authorityList)
                .build();
        return new CustomSecurityContext(userDetails);
    }
}
