package com.example.admin;

import com.example.dto.UserResponse;
import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
public class AdminController {
    @Autowired
    private UserService userService;

    @PostMapping("/{userId}/reset-password")
    public ResponseEntity<UserResponse> resetPassword(@PathVariable Integer userId) {
        User user = userService.resetPassword(userId);
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
}
