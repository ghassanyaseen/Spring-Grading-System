package com.example.springgradingsystem.controllers;

import com.example.springgradingsystem.services.AdminService;
import com.example.springgradingsystem.services.JWTService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private HttpSession session;

    @GetMapping
    public String adminDashboard(Model model) {

        String token = (String) session.getAttribute("token");

        if (token != null && jwtService.verifyToken(token)) {
            loadDashboardData(model);
            return "admin";
        } else {
            return "error";
        }
    }


    @GetMapping("/enrollS")
    public String enrollStudentPage(Model model) {
        loadDashboardData(model);
        return "admin";
    }

    private void loadDashboardData(Model model) {
        try {
            List<String> students = adminService.getAllStudents();
            model.addAttribute("students", students);

            List<String> courses = adminService.getAllCourses();
            model.addAttribute("courses", courses);

            List<String> instructors = adminService.getAllInstructors();
            model.addAttribute("instructors", instructors);
        } catch (SQLException e) {
            model.addAttribute("error", "An error occurred while fetching data.");
        }
    }


    @PostMapping("/createUser")
    public String createUser(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String type, Model model, HttpSession session) {
        try {
            String message = adminService.createUser(username, password, type);
            session.setAttribute("message", message);
        } catch (SQLException | NoSuchAlgorithmException e) {
            session.setAttribute("error", "An error occurred while creating the user.");
        }
        return "redirect:/admin";
    }

    @PostMapping("/createCourse")
    public String createCourse(@RequestParam String courseName,
                               @RequestParam String instructorUsername,
                               HttpSession session) {
        try {
            String message = adminService.createCourse(courseName, instructorUsername);
            session.setAttribute("message", message);
        } catch (SQLException e) {
            session.setAttribute("error", "An error occurred while creating the course.");
        }
        return "redirect:/admin";
    }

    @PostMapping("/enrollStudent")
    public String enrollStudent(@RequestParam String studentUsername,
                                @RequestParam String courseName,
                                HttpSession session) {
        try {
            String message = adminService.enrollStudentInTheCourse(studentUsername, courseName);
            session.setAttribute("message", message);
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "An error occurred while enrolling the student.");
        }
        return "redirect:/admin";
    }


}
