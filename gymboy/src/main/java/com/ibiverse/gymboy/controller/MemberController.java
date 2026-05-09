package com.ibiverse.gymboy.controller;

import com.ibiverse.gymboy.model.Member;
import com.ibiverse.gymboy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin/members")
public class MemberController {

    @Autowired private MemberRepository memberRepo;
    @Autowired private MembershipTierRepository tierRepo;

    @GetMapping
    public String members(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        model.addAttribute("members", memberRepo.findAll());
        model.addAttribute("tiers", tierRepo.findAll());
        return "admin/members";
    }

    @PostMapping("/add")
    public String addMember(@ModelAttribute Member member, @RequestParam(required = false) Integer tier) {
        member.setJoinDate(LocalDate.now());
        member.setStatus("Active");
        if (tier != null) {
            member.setTier(tierRepo.findById(tier).orElse(null));
        }
        memberRepo.save(member);
        return "redirect:/admin/members";
    }

    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Integer id) {
        memberRepo.deleteById(id);
        return "redirect:/admin/members";
    }

    @GetMapping("/search")
    public String searchMembers(@RequestParam String keyword, Model model) {
        model.addAttribute("members", memberRepo.findByFirstNameContainingOrLastNameContaining(keyword, keyword));
        model.addAttribute("tiers", tierRepo.findAll());
        return "admin/members";
    }
}