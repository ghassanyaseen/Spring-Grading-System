package com.example.springgradingsystem.services;

import com.example.springgradingsystem.doa.AdminDaoImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminDaoImplement adminDAO;

    @Autowired
    private HashPasswordService hashPasswordService;

    public String createUser(String username, String password, String type) throws SQLException, NoSuchAlgorithmException {
        String salt = hashPasswordService.generateSalt();

        String hashedPassword = hashPasswordService.hashPassword(password, salt);

        return adminDAO.createUser(username, hashedPassword, salt, type);
    }

    public String createCourse(String courseName, String instructorUsername) throws SQLException {
        return adminDAO.createCourse(courseName, instructorUsername);
    }

    public String enrollStudentInTheCourse(String studentUsername, String courseName) throws SQLException {
        return adminDAO.enrollStudentInTheCourse(studentUsername, courseName);
    }

    public List<String> getAllStudents() throws SQLException {
        return adminDAO.getAllStudents();
    }

    public List<String> getAllCourses() throws SQLException {
        return adminDAO.getAllCourses();
    }

    public List<String> getAllInstructors() throws SQLException {
        return adminDAO.getAllInstructors();
    }
}
