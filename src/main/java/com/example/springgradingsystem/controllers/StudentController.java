package com.example.springgradingsystem.controllers;

import com.example.springgradingsystem.services.JWTService;
import com.example.springgradingsystem.services.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;
@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private HttpSession session;

    @GetMapping()
    protected String student() {

        String token = (String) session.getAttribute("token");

        if (token != null && jwtService.verifyToken(token)) {
            return "student";
        } else {
            return "error";
        }
    }

    @PostMapping()
    protected String student(@RequestParam String options,
                             Model model) throws SQLException {

        String username = (String) session.getAttribute("username");
        String usertype = (String) session.getAttribute("usertype");

        if ("Student".equalsIgnoreCase(usertype)) {
            if ("showGrades".equalsIgnoreCase(options)) {
                List<String> studentGrades = studentService.showGrades(username);
                model.addAttribute("studentChoice", studentGrades);
                return "showGrades";
            } else if ("showGPA".equalsIgnoreCase(options)) {
                String GPA = studentService.showGPA(username);
                model.addAttribute("showGPA", GPA);
                return "showGPA";
            }
        }
        return "student";
    }
}
