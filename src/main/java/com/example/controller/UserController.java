package com.example.controller;

import com.example.dto.PageResponse;
import com.example.dto.PasswordChangeRequest;
import com.example.dto.UserRequest;
import com.example.dto.UserResponse;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.security.UserDetailsCustom;
import com.example.security.UserDetailsServiceCustom;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
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
}
