package com.example.admin;

import com.example.dto.EmailRequest;
import com.example.dto.UserResponse;
import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
public class AdminController {
    @Autowired
    private UserService userService;

    @PostMapping("/reset-password")
    public ResponseEntity<UserResponse> resetPassword(@RequestBody EmailRequest email) {
        User user = userService.resetPassword(email);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PutMapping("/{userId}/inactive")
    public ResponseEntity<UserResponse> setInactiveUser(@PathVariable Integer userId) {
        User user = userService.setInactiveUser(userId);
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PutMapping("/{userId}/active")
    public ResponseEntity<UserResponse> setActiveUser(@PathVariable Integer userId) {
        User user = userService.setActiveUser(userId);
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PostMapping("/email")
    public ResponseEntity<UserResponse> getUserByEmail (@RequestBody EmailRequest emailRequest) {
        User user = userService.getUserByEmail(emailRequest.getEmail());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(new UserResponse(user));
    }
}
