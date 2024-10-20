package com.example.springgradingsystem.model;

public class Instructor extends User {

    public Instructor(String name, String password) {
        super(name, password, UserType.INSTRUCTOR);
    }

    @Override
    public String toString(){
        return "Added Instructor Name = " + this.getName();
    }

}
