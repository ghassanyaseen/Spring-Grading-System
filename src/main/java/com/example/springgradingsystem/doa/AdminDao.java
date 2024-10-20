package com.example.springgradingsystem.doa;

public interface AdminDao {
    public String createUser(String username, String hashedPassword, String salt, String userType);
    public String createCourse(String courseName, String InstructorName);
    public String enrollStudentInTheCourse(String studentUsername, String courseName) ;
}

