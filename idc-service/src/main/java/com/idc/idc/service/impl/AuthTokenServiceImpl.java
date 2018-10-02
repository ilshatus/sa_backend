package com.idc.idc.service.impl;

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
    public static final String USER_ID = "user_id";
    private static final String JWT_TOKEN_KEY = "pizdec_haxoy_blyat";

    @Override
    public String generateToken(Long userId) {
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("clientType", "user");
        tokenData.put(USER_ID, userId);
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
    public Long getUserId(String authToken) {
        try {
            DefaultClaims claims = (DefaultClaims) Jwts.parser().setSigningKey(JWT_TOKEN_KEY).parse(authToken).getBody();
            if (claims.get(USER_ID) instanceof Integer) {
                return ((Integer) claims.get(USER_ID)).longValue();
            }
            return (Long) claims.get(USER_ID);
        } catch (Exception e) {
            return null;
        }
    }
}
