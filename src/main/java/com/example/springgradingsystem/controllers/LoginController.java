package com.example.springgradingsystem.controllers;

import com.example.springgradingsystem.services.HashPasswordService;
import com.example.springgradingsystem.services.JWTService;
import com.example.springgradingsystem.services.UserAuthenticationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    private final JWTService jwtService;

    @Autowired
    HttpSession session;

    public LoginController(UserAuthenticationService userAuthenticationService, HashPasswordService hashPasswordService, JWTService jwtService) {
        this.userAuthenticationService = userAuthenticationService;
        this.jwtService = jwtService;
    }

    @GetMapping()
    public String login() {
        return "login";
    }

    @PostMapping()
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        try {
            boolean isValid = userAuthenticationService.authenticateUser(username,password);

            if (isValid) {
                session.setAttribute("username", username);

                String usertype = userAuthenticationService.getUserType(username);

                String token = jwtService. generateToken(username, usertype);

                session.setAttribute("token", token);
                session.setAttribute("usertype", usertype);
                return selectUserInterface(usertype);

            } else {
                model.addAttribute("msg", "Invalid username or password, try to login again.");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("msg", "An unexpected error occurred.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private String selectUserInterface(String usertype) {
        String normalizedUsertype = usertype != null ? usertype.trim().toLowerCase() : "";
        return switch (normalizedUsertype) {
            case "admin" -> "redirect:/admin";
            case "student" -> "redirect:/student";
            case "instructor" -> "redirect:/instructor";
            default -> "login";
        };
    }
}
