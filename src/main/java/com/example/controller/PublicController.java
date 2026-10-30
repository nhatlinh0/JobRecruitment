package com.example.controller;

import com.example.dto.*;
import com.example.entity.User;
import com.example.security.UserDetailsCustom;
import com.example.security.UserDetailsServiceCustom;
import com.example.service.CompanyService;
import com.example.service.DomainService;
import com.example.service.SkillService;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private UserService userService;

    @Autowired
    private SkillService skillService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private CompanyService companyService;

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
        String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest userRequest) {
        try {
            User savedUser = userService.saveUser(userRequest);
            return ResponseEntity.ok(new UserResponse(savedUser));
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/skills")
    public ResponseEntity<PageResponse<SkillResponse>> getAllSkill(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(skillService.getAllSkill(pageable));
    }

    @GetMapping("/domains")
    public ResponseEntity<PageResponse<DomainResponse>> getAllDomain(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(domainService.findAll(pageable));
    }

    @GetMapping("/companies")
    public ResponseEntity<PageResponse<CompanyResponse>> getAllCompany(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(companyService.getAllCompany(pageable));
    }
}
