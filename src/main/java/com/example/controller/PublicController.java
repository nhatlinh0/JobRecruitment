package com.example.controller;

import com.example.entity.User;
import com.example.security.UserDetailsCustom;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {
    @GetMapping("/heath-check")
    public String HeathCheck() {
        return "OK";
    }

    @GetMapping ("/test")
    public String UserCheck(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (!(principal instanceof UserDetailsCustom)) {
//            return "Fail";
//        }
//        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) principal;
        User user = userDetailsCustom.getUserEntity();
        return user.getStatus() + user.getUsername() +user.getEmail();
    }
}
