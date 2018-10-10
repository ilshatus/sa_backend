package com.idc.idc.service.impl;

import com.idc.idc.User;
import com.idc.idc.UserType;
import com.idc.idc.service.AuthTokenService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthTokenServiceImpl implements AuthTokenService {
    private static final String USER_ID = "user_id";
    private static final String USER_TYPE = "user_type";
    private static final String JWT_TOKEN_KEY = "pizdec_haxoy_blyat";

    @Override
    public String generateToken(User user) {
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put(USER_ID, user.getId());
        tokenData.put(USER_TYPE, user.getUserType());
        tokenData.put("token_create_date", new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        tokenData.put("token_expiration_date", calendar.getTime());
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setExpiration(calendar.getTime());
        jwtBuilder.setClaims(tokenData);
        return jwtBuilder.signWith(SignatureAlgorithm.HS512, JWT_TOKEN_KEY).compact();
    }

    @Override
    public User getUserId(String authToken) {
        try {
            DefaultClaims claims = (DefaultClaims) Jwts.parser().setSigningKey(JWT_TOKEN_KEY).parse(authToken).getBody();
            Long id = (Long) claims.get(USER_ID);
            UserType userType = UserType.valueOf(claims.get(USER_TYPE).toString());
            return new User(id, userType);
        } catch (Exception e) {
            return null;
        }
    }
}
