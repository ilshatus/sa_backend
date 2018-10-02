package com.idc.idc.service;


public interface AuthTokenService {

    String generateToken(Long userId);

    Long getUserId(String authToken);
}
