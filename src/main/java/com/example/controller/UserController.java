package com.example.controller;

import com.example.dto.*;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.security.UserDetailsCustom;
import com.example.security.UserDetailsServiceCustom;
import com.example.service.ApplicationService;
import com.example.service.JobService;
import com.example.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JobService jobService;

    @Autowired
    private ApplicationService applicationService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all-users")
    public ResponseEntity<PageResponse<UserResponse>> findByRoleIdNot(Integer id,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(userService.findByRoleIdNot(4, pageable));
    }

    @PostMapping("/get-user")
    public ResponseEntity<UserResponse> getUserByEmail(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        User user = userDetailsCustom.getUserEntity();
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser (@RequestBody String username, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        try {
            User updatedUser = userService.updateUser(username, userDetailsCustom.getUserEntity());
            return ResponseEntity.ok(new UserResponse(updatedUser));
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/password-change")
    public ResponseEntity<?> changePassword (@RequestBody PasswordChangeRequest password, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        try {
            userService.updateUserPassword(password, userDetailsCustom.getUserEntity());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/saved/{id}")
    public ResponseEntity<?> savedJob(@PathVariable Integer id, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        jobService.addSavedJob(id, userDetailsCustom.getUserEntity());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/application-jobs")
    public ResponseEntity<PageResponse<ApplicationResponse>> findApplications(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
                                                                Pageable pageable) {
        PageResponse<ApplicationResponse> list = applicationService.findUserApplication(userDetailsCustom.getUserEntity(), pageable);
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/saved-jobs")
    public ResponseEntity<PageResponse<JobResponse>> findSavedJobs(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
    Pageable pageable) {
        PageResponse<JobResponse> list = jobService.findSavedJobs(userDetailsCustom.getUserEntity(), pageable);
        return ResponseEntity.ok(list);
    }
}
