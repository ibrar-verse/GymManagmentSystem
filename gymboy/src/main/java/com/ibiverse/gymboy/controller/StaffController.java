package com.ibiverse.gymboy.controller;

import com.ibiverse.gymboy.model.Staff;
import com.ibiverse.gymboy.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/admin/staff")
public class StaffController {

    @Autowired private StaffRepository staffRepo;
    
    private static final Path UPLOAD_DIR = Paths.get(System.getProperty("user.dir"), "uploads", "staff");

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
                Files.createDirectories(UPLOAD_DIR);

                String originalFileName = photo.getOriginalFilename();
                String safeName = originalFileName == null ? "staff-photo" : originalFileName.replaceAll("[^a-zA-Z0-9._-]", "_");
                String fileName = UUID.randomUUID() + "_" + safeName;
                Path filePath = UPLOAD_DIR.resolve(fileName);
                photo.transferTo(filePath);
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