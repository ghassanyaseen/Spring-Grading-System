package com.example.springgradingsystem.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
public class UserAuthenticationService {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private final HashPasswordService hashPasswordService;

    public UserAuthenticationService(JdbcTemplate jdbcTemplate, HashPasswordService hashPasswordService) {
        this.jdbcTemplate = jdbcTemplate;
        this.hashPasswordService = hashPasswordService;
    }

    public boolean authenticateUser(String username, String enteredPassword) throws NoSuchAlgorithmException {
        String sql = " SELECT password, salt FROM Users WHERE username = ?";

        Map<String, Object> result = jdbcTemplate.queryForMap(sql, username);

        if (result == null || result.isEmpty()) {
            return false;
        }

        String storedHashedPassword = (String) result.get("password");
        String salt = (String) result.get("salt");

        return hashPasswordService.verifyPassword(enteredPassword, storedHashedPassword, salt);
    }
    
    public String getUserType(String username) {
        String sql = """
            SELECT userType
            FROM Users
            WHERE username = ?
        """;

        return jdbcTemplate.queryForObject(sql, String.class, username);
    }

    public String getStoredPassword(String username) {
        String sql = """
        SELECT password
        FROM Users
        WHERE username = ?
    """;
        return jdbcTemplate.queryForObject(sql, String.class, username);
    }

    public String getSalt(String username) {
        String sql = """
        SELECT salt
        FROM Users
        WHERE username = ?
    """;
        return jdbcTemplate.queryForObject(sql, String.class, username);
    }

}
