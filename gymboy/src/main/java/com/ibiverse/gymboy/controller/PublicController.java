package com.ibiverse.gymboy.controller;

import com.ibiverse.gymboy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/public")
public class PublicController {

    @Autowired private FacilityRepository facilityRepo;
    @Autowired private StaffRepository staffRepo;
    @Autowired private MembershipTierRepository tierRepo;
    @Autowired private GymClassRepository classRepo;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("facilities", facilityRepo.findAll());
        model.addAttribute("trainers", staffRepo.findByRole("Trainer"));
        model.addAttribute("tiers", tierRepo.findAll());
        model.addAttribute("classes", classRepo.findAll());
        return "public/home";
    }
}