package com.example.springgradingsystem.services;

import com.example.springgradingsystem.doa.StudentDaoImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class StudentService {

    @Autowired
    private StudentDaoImplement studentDoa;

    public ArrayList<String> showGrades(String username) throws SQLException {
        return studentDoa.showGrades(username);
    }

    public String showGPA(String username) throws SQLException {
        return studentDoa.showGPA(username);
    }

}
