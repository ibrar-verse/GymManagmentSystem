package com.ibiverse.gymboy.controller;

import com.ibiverse.gymboy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired private MemberRepository memberRepo;
    @Autowired private CheckInLogRepository checkInRepo;
    @Autowired private MembershipTierRepository tierRepo;
    @Autowired private StaffRepository staffRepo;

    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        
        model.addAttribute("totalMembers", memberRepo.count());
        model.addAttribute("totalCheckIns", checkInRepo.count());
        model.addAttribute("totalTiers", tierRepo.count());
        model.addAttribute("totalStaff", staffRepo.count());
        
        return "admin/dashboard";
    }
}