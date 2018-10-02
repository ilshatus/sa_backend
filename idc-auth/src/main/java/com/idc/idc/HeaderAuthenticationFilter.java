package com.idc.idc;

import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.User;
import com.idc.idc.service.UserService;
import com.idc.idc.service.AuthTokenService;
import lombok.Getter;
import lombok.Setter;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private AuthTokenService authTokenService;
    private UserService userService;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        SecurityContext securityContext = loadSecurityContext(request);
        SecurityContextHolder.setContext(securityContext);

        chain.doFilter(request, response);
    }

    private SecurityContext loadSecurityContext(HttpServletRequest request) {
        Long userId = null;
        String xAuth = request.getHeader("Authorization");
        String clientId = request.getHeader("X-ClientId");

        if (StringUtils.isNotBlank(xAuth)) {
            try {
                userId = authTokenService.getUserId(xAuth);
            } catch (IndexOutOfBoundsException e) {
                logger.info("Not valid token");
            }
        }
        User user = null;
        try {
            user = userService.getUser(userId);
        } catch (NotFoundException e) {
            LOGGER.info("User with id {} not found", userId);
        }
        if (user != null) {
            List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList();
            authorityList.add(new SimpleGrantedAuthority(user.getRole().name()));
            authorityList.add(new SimpleGrantedAuthority(user.getState().name()));
            UserDetails userDetails = CurrentUser
                    .builder()
                    .id(userId)
                    .username(userId != null ? userId.toString() : null)
                    .clientId(clientId)
                    .enabled(true)
                    .accountNonLocked(true)
                    .accountNonExpired(true)
                    .credentialsNonExpired(true)
                    .authorities(authorityList)
                    .build();

            return new CustomSecurityContext(userDetails);
        } else {
            UserDetails userDetails = CurrentUser
                    .builder()
                    .id(null)
                    .username(CurrentUser.ANONYMOUS_AUTHORITY)
                    .clientId(clientId)
                    .enabled(true)
                    .accountNonLocked(true)
                    .accountNonExpired(true)
                    .credentialsNonExpired(true)
                    .authorities(AuthorityUtils.createAuthorityList(CurrentUser.ANONYMOUS_AUTHORITY))
                    .build();

            return new CustomSecurityContext(userDetails);
        }
    }
}
