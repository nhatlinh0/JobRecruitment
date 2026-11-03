package com.example.controller;

import com.example.dto.ApplicationRequest;
import com.example.dto.ApplicationResponse;
import com.example.dto.PageResponse;
import com.example.dto.UpdateStatusRequest;
import com.example.entity.Application;
import com.example.enums.Status;
import com.example.security.UserDetailsCustom;
import com.example.service.ApplicationService;
import com.example.service.CloudinaryService;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping()
    public ResponseEntity<PageResponse<ApplicationResponse>> getAllApplication (@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        PageResponse<ApplicationResponse> list = applicationService.getAllApplications(pageable);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getApplicationById (@PathVariable Integer id) {
        ApplicationResponse application = applicationService.getApplicationById(id);
        return new ResponseEntity<>(application, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public ResponseEntity<PageResponse<ApplicationResponse>> getAllApplicationByUser (@AuthenticationPrincipal UserDetailsCustom userDetailsCustom,
                                                                              @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        PageResponse<ApplicationResponse> list = applicationService.getAllApplicationsByUser(userDetailsCustom.getUserEntity(), pageable);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RECRUIT')")
    @GetMapping("/job/{id}")
    public ResponseEntity<PageResponse<ApplicationResponse>> getAllApplicationByJob (@PathVariable Integer id, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom,
                                                                             @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        PageResponse<ApplicationResponse> list = applicationService.getAllApplicationsByJob(id, userDetailsCustom.getUserEntity(), pageable);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public ResponseEntity<?> saveApplication (@Valid @ModelAttribute ApplicationRequest applicationRequest, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        String imageUrl = applicationRequest.getCvUrl();

        if (applicationRequest.getCvUrl() == null || applicationRequest.getCvUrl().isEmpty()) {
            imageUrl = cloudinaryService.uploadImage(applicationRequest.getCvFile());
        }
        applicationService.saveApplication(applicationRequest, imageUrl, userDetailsCustom.getUserEntity());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplication (@RequestBody ApplicationRequest applicationRequest, @PathVariable Integer id, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        applicationService.updateApplication(applicationRequest, id, userDetailsCustom.getUserEntity());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication (@PathVariable Integer id) {
        applicationService.deleteApplication(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/exist/{jobId}")
    public ResponseEntity<?> existApplication (@PathVariable Integer jobId, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        if (applicationService.exitsApplication(userDetailsCustom.getUserEntity().getId(), jobId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('RECRUIT')")
    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateApplicationStatus (@RequestBody UpdateStatusRequest status, @PathVariable Integer id, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        applicationService.updateApplicationStatus(status, id, userDetailsCustom.getUserEntity());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
