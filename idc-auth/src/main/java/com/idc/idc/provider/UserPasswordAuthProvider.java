package com.idc.idc.provider;

import com.idc.idc.AuthenticationImpl;
import com.idc.idc.CurrentUser;
import com.idc.idc.RoleUsernamePasswordAuthToken;
import com.idc.idc.UserType;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.abstracts.AbstractUserEntity;
import com.idc.idc.model.users.Operator;
import com.idc.idc.service.UserService;
import com.idc.idc.util.PasswordUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
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
        RoleUsernamePasswordAuthToken authToken = (RoleUsernamePasswordAuthToken) authentication;

        Object passwordObj = authToken.getCredentials();
        if (passwordObj == null) {
            passwordObj = StringUtils.EMPTY;
        }
        String password = passwordObj.toString();
        String email = authToken.getName();

        if (StringUtils.isAnyBlank(password, email)) {
            throw new BadCredentialsException("empty-param");
        }

        UserType userType = authToken.getUserType();
        if (userType == null) {
            throw new BadCredentialsException("empty-role");
        }


        AbstractUserEntity userEntity = null;
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList();
        try {
            switch (userType) {
                case CUSTOMER:
                    userEntity = userService.getCustomerByEmail(email);
                    break;
                case DRIVER:
                    userEntity = userService.getDriverByEmail(email);
                    break;
                case OPERATOR:
                    Operator operator = userService.getOperatorByEmail(email);
                    userEntity = operator;
                    if (operator.getAdmin())
                        authorityList.add(new SimpleGrantedAuthority("ADMIN"));
            }
        } catch (NotFoundException e) {
            throw new BadCredentialsException("user-not-found");
        }

        if (!(Objects.equals(passwordUtil.getHash(password), userEntity.getPasswordHash()))) {
            throw new BadCredentialsException("wrong-passwordHash");
        }

        authorityList.add(new SimpleGrantedAuthority(userType.name()));
        UserDetails currentUser = CurrentUser
                .builder()
                .id(userEntity.getId())
                .userType(userType)
                .username(userEntity.getId().toString())
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
        return StringUtils.equals(authentication.getName(), RoleUsernamePasswordAuthToken.class.getName());
    }
}
