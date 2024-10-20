package com.example.springgradingsystem.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JWTUnit {

    public static String createToken(String username, String usertype, String secretKey, long expirationTimeInMillis) {
        return Jwts.builder()
                .setSubject(username)
                .claim("usertype", usertype)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMillis))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public static boolean validateToken(String token, String secretKey) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}