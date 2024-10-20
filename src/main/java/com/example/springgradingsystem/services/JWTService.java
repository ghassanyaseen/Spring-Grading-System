package com.example.springgradingsystem.services;

import com.example.springgradingsystem.config.JWTUnit;
import org.springframework.stereotype.Service;


@Service
public class JWTService {

    private static final String SECRET_KEY = "GHASSAN_KEY";

    public String getSecretKey() {
        return SECRET_KEY;
    }

    public String generateToken(String username, String role) {
        String secretKey = getSecretKey();
        long expirationTimeInMillis = 3600000;
        return JWTUnit.createToken(username, role, secretKey, expirationTimeInMillis);
    }

    public boolean verifyToken(String token) {
        String secretKey = getSecretKey();
        return JWTUnit.validateToken(token, secretKey);
    }
}
