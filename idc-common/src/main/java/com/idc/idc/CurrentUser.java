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
    public static final String USER_AUTHORITY = "USER";
    public static final String ANONYMOUS_AUTHORITY = "ANONYMOUS";
    private static final long serialVersionUID = 1299142021617783779L;

    private Long id;
    private String clientId;

    @Builder
    public CurrentUser(
            Long id,
            String username,
            String clientId,
            boolean enabled,
            boolean accountNonLocked,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            Collection<? extends GrantedAuthority> authorities) {

        super(username, "", enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        setId(id);
        setClientId(clientId);
    }
}
