package com.idc.idc.config;

import com.idc.idc.HeaderAuthenticationFilter;
import com.idc.idc.UserType;
import com.idc.idc.provider.UserPasswordAuthProvider;
import com.idc.idc.service.AuthTokenService;
import com.idc.idc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final AuthTokenService authTokenService;
    private final UserPasswordAuthProvider userPasswordAuthProvider;

    @Autowired
    public WebSecurityConfig(AuthTokenService authTokenService, UserService userService, UserPasswordAuthProvider userPasswordAuthProvider) {
        this.authTokenService = authTokenService;
        this.userService = userService;
        this.userPasswordAuthProvider = userPasswordAuthProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        HeaderAuthenticationFilter filter = new HeaderAuthenticationFilter();
        filter.setAuthTokenService(authTokenService);
        filter.setUserService(userService);
        http
                .csrf().disable()
                .requestCache().disable()
                .rememberMe().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(filter)
                .authorizeRequests()
                .antMatchers("/**.js", "/**.html").permitAll()
                .antMatchers("/v1/admin/**").hasAuthority("ADMIN")
                .antMatchers("/v1/operators").permitAll()
                .antMatchers("/v1/drivers").permitAll()
                .antMatchers("/v1/customers").permitAll()
                .antMatchers("/v1/orders").permitAll()
                .and()
                .logout().permitAll();
    }



    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userPasswordAuthProvider);
    }
}
