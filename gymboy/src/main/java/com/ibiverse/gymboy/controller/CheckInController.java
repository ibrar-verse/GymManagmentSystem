package com.ibiverse.gymboy.controller;

import com.ibiverse.gymboy.model.CheckInLog;
import com.ibiverse.gymboy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/checkin")
public class CheckInController {

    @Autowired private CheckInLogRepository checkInRepo;
    @Autowired private MemberRepository memberRepo;

    @GetMapping
    public String checkInPage(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        model.addAttribute("logs", checkInRepo.findAll());
        return "admin/checkin";
    }

    @PostMapping("/log")
    public String logCheckIn(@RequestParam Integer memberId) {
        CheckInLog log = new CheckInLog();
        log.setMember(memberRepo.findById(memberId).orElse(null));
        log.setCheckInTime(LocalDateTime.now());
        checkInRepo.save(log);
        return "redirect:/admin/checkin";
    }
}