package com.ibiverse.gymboy.controller;

import com.ibiverse.gymboy.model.MembershipTier;
import com.ibiverse.gymboy.repository.MembershipTierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/tiers")
public class TierController {

    @Autowired private MembershipTierRepository tierRepo;

    @GetMapping
    public String tiers(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        model.addAttribute("tiers", tierRepo.findAll());
        return "admin/tiers";
    }

    @PostMapping("/add")
    public String addTier(@ModelAttribute MembershipTier tier) {
        tierRepo.save(tier);
        return "redirect:/admin/tiers";
    }

    @GetMapping("/delete/{id}")
    public String deleteTier(@PathVariable Integer id) {
        tierRepo.deleteById(id);
        return "redirect:/admin/tiers";
    }
}
