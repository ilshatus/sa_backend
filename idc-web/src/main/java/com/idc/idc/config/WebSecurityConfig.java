package com.idc.idc.config;

import com.idc.idc.HeaderAuthenticationFilter;
import com.idc.idc.UserType;
import com.idc.idc.provider.UserPasswordAuthProvider;
import com.idc.idc.service.AuthTokenService;
import com.idc.idc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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
                .cors()
                .and()
                .csrf().disable()
                .requestCache().disable()
                .rememberMe().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(filter)
                .authorizeRequests()
                .antMatchers("/**.js", "/**.html", "/**/login", "/**/register").permitAll()
                .antMatchers("/v1/admin/**").hasAuthority("ADMIN")
                .antMatchers("/v1/operator/**").hasAuthority(UserType.OPERATOR.name())
                .antMatchers("/v1/driver/**").hasAuthority(UserType.DRIVER.name())
                .antMatchers("/v1/customer/**").hasAuthority(UserType.CUSTOMER.name())
                .antMatchers("/v1/**").permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(new UnauthorizedEntryPoint())
                .and()
                .logout().permitAll();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }


    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(userPasswordAuthProvider);
    }

    public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

        public UnauthorizedEntryPoint() {
        }

        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2) throws IOException, ServletException {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
    }
}
