package com.ibiverse.gymboy.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Value("${admin.username:ibiverse}")
    private String adminUsername;

    @Value("${admin.password:Ibi@2024}")
    private String adminPassword;

    @GetMapping("/")
    public String home() {
        return "redirect:/public/home";
    }

    @GetMapping("/home")
    public String legacyHome() {
        return "redirect:/public/home";
    }

    @GetMapping("/admin/login")
    public String loginPage() {
        return "admin/login";
    }

    @PostMapping("/admin/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password,
                       HttpSession session, Model model) {
        if (adminUsername.equals(username) && adminPassword.equals(password)) {
            session.setAttribute("admin", true);
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("error", "Invalid credentials!");
        return "admin/login";
    }

    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}