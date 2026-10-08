package com.example.controller;

import com.example.dto.JobRequest;
import com.example.dto.JobResponse;
import com.example.entity.Job;
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

//    ADMIN
    @GetMapping("/id/{id}")
    public ResponseEntity<Job> findJobById(@PathVariable Integer id) {
        Job job = jobService.findJobById(id);
        return ResponseEntity.ok(job);
    }
//-------------
    @GetMapping("/skill/{skillSlug}")
    public ResponseEntity<List<JobResponse>> findJobBySkillSlug(@PathVariable String skillSlug) {
        List<JobResponse> list = jobService.findJobsBySkillSlug(skillSlug);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/domain/{domainSlug}")
    public ResponseEntity<?> findJobByDomainSlug(@PathVariable String domainSlug) {
        List<JobResponse> list = jobService.findJobsByDomainSlug(domainSlug);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/slug/{Slug}")
    public ResponseEntity<JobResponse> findJobBySlug(@PathVariable String slug) {
         Job job = jobService.findJobBySlug(slug);
        return ResponseEntity.ok(new JobResponse(job));
    }
//------------
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

//    @DeleteMapping("/{jobId}/skill/{skillId}")
//    public ResponseEntity<?> deleteJobSkills(@PathVariable Integer jobId, @PathVariable Integer skillId) {
//        jobService.deleteJobSkills(jobId, skillId);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
