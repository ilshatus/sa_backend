package com.idc.idc;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Setter
@Getter
public class CurrentUser extends User {
    private Long id;
    private UserType userType;

    @Builder
    public CurrentUser(
            Long id,
            String username,
            UserType userType,
            boolean enabled,
            boolean accountNonLocked,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            Collection<? extends GrantedAuthority> authorities) {

        super(username, "", enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        setId(id);
        setUserType(userType);
    }
}
