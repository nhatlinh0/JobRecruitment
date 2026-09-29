package com.example.controller;

import com.example.dto.UserInfoRequest;
import com.example.dto.UserInfoResponse;
import com.example.entity.UserInfo;
import com.example.repository.UserRepository;
import com.example.service.UserInfoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-info")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    //BASIC
    @GetMapping("/{id}")
    public ResponseEntity<UserInfoResponse> getUserInfo(@PathVariable Integer id) {
        UserInfo userInfo = userInfoService.findUserInfo(id);
        return new ResponseEntity<>(new UserInfoResponse(userInfo), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UserInfoResponse> createUserInfo(@Valid @RequestBody UserInfoRequest userInfoRequest) {
//        BASIC
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            UserInfo userInfo = userInfoService.saveUserInfo(userInfoRequest, userEmail);
            return new ResponseEntity<>(new UserInfoResponse(userInfo),HttpStatus.CREATED);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping
    public ResponseEntity<UserInfoResponse> updateUserInfo(@Valid @RequestBody UserInfoRequest userInfoRequest) {
//        BASIC
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            UserInfo userInfo = userInfoService.updateUserInfo(userInfoRequest, userEmail);
            return new ResponseEntity<>(new UserInfoResponse(userInfo),HttpStatus.OK);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserInfo(@PathVariable Integer id) {
        UserInfo userInfo = userInfoService.findUserInfo(id);
        if (userInfo != null) {
            userInfoService.deleteUserInfo(userInfo);
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
}
