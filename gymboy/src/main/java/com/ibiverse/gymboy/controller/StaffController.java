package com.ibiverse.gymboy.controller;

import com.ibiverse.gymboy.model.Staff;
import com.ibiverse.gymboy.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.util.UUID;

@Controller
@RequestMapping("/admin/staff")
public class StaffController {

    @Autowired private StaffRepository staffRepo;
    
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/staff/";

    @GetMapping
    public String staff(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        model.addAttribute("staff", staffRepo.findAll());
        return "admin/staff";
    }

    @PostMapping("/add")
    public String addStaff(HttpSession session, @ModelAttribute Staff staff, @RequestParam(required = false) MultipartFile photo) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        
        // Handle photo upload
        if (photo != null && !photo.isEmpty()) {
            try {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) uploadDir.mkdirs();
                
                String fileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
                File file = new File(uploadDir + fileName);
                photo.transferTo(file);
                staff.setPhotoPath("/uploads/staff/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        staffRepo.save(staff);
        return "redirect:/admin/staff";
    }

    @GetMapping("/delete/{id}")
    public String deleteStaff(HttpSession session, @PathVariable Integer id) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        staffRepo.deleteById(id);
        return "redirect:/admin/staff";
    }
}