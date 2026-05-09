package com.ibiverse.gymboy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckInPageController {

    @GetMapping("/check-in")
    public String checkInPage() {
        return "check-in";
    }
}