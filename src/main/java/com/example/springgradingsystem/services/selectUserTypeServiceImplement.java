package com.example.springgradingsystem.services;

import com.example.springgradingsystem.model.Admin;
import com.example.springgradingsystem.model.Instructor;
import com.example.springgradingsystem.model.Student;
import com.example.springgradingsystem.model.User;


public class selectUserTypeServiceImplement {
    public static User selectUserType(String username , String password, String usertype) {
        if (usertype.equalsIgnoreCase("student")) {
            return new Student(username, password);
        } else if (usertype.equalsIgnoreCase("instructor")) {
            return new Instructor(username, password);
        } else if (usertype.equalsIgnoreCase("admin")) {
            return new Admin(username, password);
        }else return null;
    }
}
