package com.example.controller;

import com.example.dto.JobRequest;
import com.example.dto.JobResponse;
import com.example.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {
    @Autowired
    private JobService jobService;

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        List<JobResponse> list = jobService.getAllJobs();
        if (!list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findJobById() {

    }

    @GetMapping("/{skillSlug}")
    public ResponseEntity<?> findJobBySkillSlug() {

    }

    @GetMapping("/{domainSlug}")
    public ResponseEntity<?> findJobByDomainSlug() {

    }

    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody JobRequest jobRequest) {
        jobService.saveJob(jobRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@RequestBody JobRequest jobRequest, @PathVariable Integer id) {
        jobService.updateJob(jobRequest, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Integer id) {
        jobService.deleteJob(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{jobId}/skill/{skillId}")
    public ResponseEntity<?> deleteJobSkills(@PathVariable Integer jobId, @PathVariable Integer skillId) {
        jobService.deleteJobSkills(jobId, skillId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
