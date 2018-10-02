package com.idc.idc.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
    public static final int DEFAULT_PASSWORD_LENGTH = 10;

    public String generate() {
        return generate(DEFAULT_PASSWORD_LENGTH);
    }

    public String generate(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public String getHash(String password) {
        return DigestUtils.sha256Hex(password);
    }
}
