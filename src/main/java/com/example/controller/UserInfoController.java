package com.example.controller;

import com.example.dto.UserInfoRequest;
import com.example.dto.UserInfoResponse;
import com.example.entity.UserInfo;
import com.example.repository.UserRepository;
import com.example.security.UserDetailsCustom;
import com.example.service.UserInfoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user-infos")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping()
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        UserInfo userInfo = userInfoService.findUserInfo(userDetailsCustom.getUserEntity());
        return new ResponseEntity<>(new UserInfoResponse(userInfo), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public ResponseEntity<UserInfoResponse> createUserInfo(@Valid @RequestBody UserInfoRequest userInfoRequest, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
//        BASIC
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            UserInfo userInfo = userInfoService.saveUserInfo(userInfoRequest, userDetailsCustom.getUserEntity());
            return new ResponseEntity<>(new UserInfoResponse(userInfo),HttpStatus.CREATED);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasRole(USER')")
    @PutMapping
    public ResponseEntity<UserInfoResponse> updateUserInfo(@Valid @RequestBody UserInfoRequest userInfoRequest, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
//        BASIC
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            UserInfo userInfo = userInfoService.updateUserInfo(userInfoRequest, userDetailsCustom.getUserEntity());
            return new ResponseEntity<>(new UserInfoResponse(userInfo),HttpStatus.OK);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
