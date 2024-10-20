package com.example.springgradingsystem.services;

import com.example.springgradingsystem.model.User;

public interface selectUserTypeService {
    public User selectUserType(String username , String password, String usertype);

}
