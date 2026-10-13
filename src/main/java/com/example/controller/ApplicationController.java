package com.example.controller;

import com.example.dto.ApplicationRequest;
import com.example.dto.ApplicationResponse;
import com.example.entity.Application;
import com.example.security.UserDetailsCustom;
import com.example.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @GetMapping()
    public ResponseEntity<List<ApplicationResponse>> getAllApplication () {
        List<ApplicationResponse> list = applicationService.getAllApplications();
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<ApplicationResponse> getApplicationById (@PathVariable Integer id) {
        ApplicationResponse application = applicationService.getApplicationById(id);
        return new ResponseEntity<>(application, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ApplicationResponse>> getAllApplicationByUser (@AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        List<ApplicationResponse> list = applicationService.getAllApplicationsByUser(userDetailsCustom.getUserEntity());
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/job/{id}")
    public ResponseEntity<List<ApplicationResponse>> getAllApplicationByJob (@PathVariable Integer id, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        List<ApplicationResponse> list = applicationService.getAllApplicationsByJob(id, userDetailsCustom.getUserEntity());
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> saveApplication (@RequestBody ApplicationRequest applicationRequest, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        applicationService.saveApplication(applicationRequest, userDetailsCustom.getUserEntity());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplication (@RequestBody ApplicationRequest applicationRequest, @PathVariable Integer id, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        applicationService.updateApplication(applicationRequest, id, userDetailsCustom.getUserEntity());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication (@PathVariable Integer id) {
        applicationService.deleteApplication(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
