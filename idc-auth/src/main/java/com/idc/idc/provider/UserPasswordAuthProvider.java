package com.idc.idc.provider;

import com.idc.idc.AuthenticationImpl;
import com.idc.idc.CurrentUser;
import com.idc.idc.model.User;
import com.idc.idc.service.UserService;
import com.idc.idc.util.PasswordUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class UserPasswordAuthProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordUtil passwordUtil;

    @Autowired
    public UserPasswordAuthProvider(UserService userService, PasswordUtil passwordUtil) {
        this.userService = userService;
        this.passwordUtil = passwordUtil;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object passwordObj = authentication.getCredentials();
        if (passwordObj == null) {
            passwordObj = StringUtils.EMPTY;
        }
        String password = passwordObj.toString();
        String email = authentication.getName();
        if (StringUtils.isAnyBlank(password, email)) {
            throw new BadCredentialsException("empty-param");
        }

        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new BadCredentialsException("user-not-found");
        }
        if (!(Objects.equals(passwordUtil.getHash(password), user.getPasswordHash()))) {
            throw new BadCredentialsException("wrong-passwordHash");
        }

        Long userId = user.getId();
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList();
        authorityList.add(new SimpleGrantedAuthority(user.getRole().name()));
        authorityList.add(new SimpleGrantedAuthority(user.getState().name()));
        UserDetails currentUser = CurrentUser
                .builder()
                .id(userId)
                .username(userId.toString())
                .clientId(null)
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .authorities(authorityList)
                .build();

        return new AuthenticationImpl(currentUser);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return StringUtils.equals(authentication.getName(), UsernamePasswordAuthenticationToken.class.getName());
    }
}
