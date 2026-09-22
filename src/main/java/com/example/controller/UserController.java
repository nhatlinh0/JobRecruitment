package com.example.controller;

import com.example.dto.UserRequest;
import com.example.dto.UserResponse;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;;

    @GetMapping("/get-all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
         List<UserResponse> userResponseList = userService.getAllUsers();
        return ResponseEntity.ok(userResponseList);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest userRequest) {
        try {
            User savedUser = userService.saveUser(userRequest);
            return ResponseEntity.ok(new UserResponse(savedUser));
        } catch (Exception e) {
            log.error("Error: "+e);
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser (@Valid @RequestBody UserRequest userRequest) {
        try {
            User updatedUser = userService.updateUser(userRequest);
            return ResponseEntity.ok(new UserResponse(updatedUser));
        } catch (Exception e) {
            log.error("Error: "+e);
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
