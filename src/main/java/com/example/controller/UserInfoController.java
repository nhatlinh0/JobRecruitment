package com.example.controller;

import com.example.dto.UserInfoRequest;
import com.example.dto.UserInfoResponse;
import com.example.entity.UserInfo;
import com.example.repository.UserRepository;
import com.example.security.UserDetailsCustom;
import com.example.service.CloudinaryService;
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

    @Autowired
    private CloudinaryService cloudinaryService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping()
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        UserInfo userInfo = userInfoService.findUserInfo(userDetailsCustom.getUserEntity());
        if (userInfo != null) {
            return new ResponseEntity<>(new UserInfoResponse(userInfo), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public ResponseEntity<UserInfoResponse> createUserInfo(@Valid @ModelAttribute UserInfoRequest userInfoRequest, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        try {
            String imageUrl = null;

            if (userInfoRequest.getImage() != null && !userInfoRequest.getImage().isEmpty()) {
                imageUrl = cloudinaryService.uploadImage(userInfoRequest.getImage());
            }
            UserInfo userInfo = userInfoService.saveUserInfo(userInfoRequest, imageUrl, userDetailsCustom.getUserEntity());
            return new ResponseEntity<>(new UserInfoResponse(userInfo),HttpStatus.CREATED);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasRole(USER')")
    @PutMapping
    public ResponseEntity<UserInfoResponse> updateUserInfo(@Valid @ModelAttribute UserInfoRequest userInfoRequest, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        try {
            String imageUrl = null;

            if (userInfoRequest.getImage() != null && !userInfoRequest.getImage().isEmpty()) {
                imageUrl = cloudinaryService.uploadImage(userInfoRequest.getImage());
            }
            UserInfo userInfo = userInfoService.updateUserInfo(userInfoRequest, imageUrl, userDetailsCustom.getUserEntity());
            return new ResponseEntity<>(new UserInfoResponse(userInfo),HttpStatus.OK);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
