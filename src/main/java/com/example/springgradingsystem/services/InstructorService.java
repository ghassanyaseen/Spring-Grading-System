package com.example.springgradingsystem.services;

import com.example.springgradingsystem.doa.InstructorDaoImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.springgradingsystem.model.*;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class InstructorService {

    @Autowired
    private InstructorDaoImplement instructorDAO;

    public ArrayList<String> showInstructorCourses(String instructorUsername) throws SQLException {
        return instructorDAO.showInstructorCourses(instructorUsername);
    }

    public ArrayList<String> showStudentsInTheCourse(String courseName, String instructorUsername) throws SQLException {
        return instructorDAO.showStudentsInTheCourse(courseName, instructorUsername);
    }

    public String enterStudentGrades(String courseName, String studentUsername, int grade) throws SQLException {
        return instructorDAO.enterStudentGrades(courseName, studentUsername, grade);
    }

    public ArrayList<StudentGrade> showStudentsWithCourse(String courseName, String instructorUsername) {
        return instructorDAO.showStudentsWithCourse(courseName, instructorUsername);
    }

}
