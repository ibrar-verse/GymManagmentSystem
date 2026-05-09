package com.ibiverse.gymboy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @GetMapping("/")
    public String home() {
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
        if ("ibiverse".equals(username) && "Ibi@2024".equals(password)) {
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