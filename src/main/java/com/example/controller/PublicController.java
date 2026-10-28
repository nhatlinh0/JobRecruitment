package com.example.controller;

import com.example.dto.UserRequest;
import com.example.entity.User;
import com.example.security.UserDetailsCustom;
import com.example.security.UserDetailsServiceCustom;
import com.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private  UserDetailsServiceCustom userDetailsServiceCustom;

    @Autowired
    private JwtUtil jwtUtil;


    @GetMapping("/heath-check")
    public String HeathCheck() {
        return "OK";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                )
        );
        UserDetails userDetails = userDetailsServiceCustom.loadUserByUsername(user.getEmail());
        String jwtToken = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getUsername());
        return ResponseEntity.ok(jwtToken);
    }

}
