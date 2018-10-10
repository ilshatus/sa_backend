package com.idc.idc.service;


import com.idc.idc.User;

public interface AuthTokenService {

    String generateToken(User user);
    User getUserId(String authToken);
}
