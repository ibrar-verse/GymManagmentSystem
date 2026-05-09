package com.ibiverse.gymboy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest req, Exception ex, Model model) {
        log.error("Unhandled exception for request {} {}", req.getMethod(), req.getRequestURI(), ex);
        model.addAttribute("timestamp", java.time.ZonedDateTime.now());
        model.addAttribute("uri", req.getRequestURI());
        model.addAttribute("message", ex.getMessage());
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement el : ex.getStackTrace()) {
            sb.append(el.toString()).append("\n");
        }
        model.addAttribute("stacktrace", sb.toString());
        return "error";
    }
}
