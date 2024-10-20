package com.example.springgradingsystem.controllers;

import com.example.springgradingsystem.model.StudentGrade;
import com.example.springgradingsystem.services.InstructorService;
import com.example.springgradingsystem.services.JWTService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.ArrayList;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private HttpSession session;

    @GetMapping
    protected String instructor() {

        String token = (String) session.getAttribute("token");

        if (token != null && jwtService.verifyToken(token)) {
            return "instructor";
        }else {
            return "error";
        }
    }

    @PostMapping
    protected String handleAction(@RequestParam String action, Model model) throws SQLException {
        String username = (String) session.getAttribute("username");

        switch (action) {
            case "showCourses":
                ArrayList<String> courses = instructorService.showInstructorCourses(username);
                model.addAttribute("courses", courses);
                return "showCourses";
            case "CoursesManagement":
                ArrayList<String> coursesForStudents = instructorService.showInstructorCourses(username);
                model.addAttribute("courses", coursesForStudents);
                return "selectCourseForStudents";

            default:
                return "error";
        }
    }

    @PostMapping("/viewStudents")
    protected String viewStudents(@RequestParam String courseName, Model model) throws SQLException {
        String username = (String) session.getAttribute("username");

        ArrayList<StudentGrade> studentsWithGrades = instructorService.showStudentsWithCourse(courseName, username);

        model.addAttribute("students", studentsWithGrades);
        model.addAttribute("courseName", courseName);

        return "showStudents";
    }


    @PostMapping("/enterGrades")
    protected String enterGrades(@RequestParam String courseName, @RequestParam String studentUsername,
                                 @RequestParam int grade, Model model) throws SQLException {

        String result = instructorService.enterStudentGrades(courseName, studentUsername, grade);
        model.addAttribute("result", result);
        return "gradeResult";
    }
}
