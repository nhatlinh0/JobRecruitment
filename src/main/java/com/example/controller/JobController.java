package com.example.controller;

import com.example.dto.JobFilterRequest;
import com.example.dto.JobRequest;
import com.example.dto.JobResponse;
import com.example.dto.PageResponse;
import com.example.entity.Job;
import com.example.security.UserDetailsCustom;
import com.example.service.JobService;
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
@RequestMapping("/api/v1/jobs")
public class JobController {
    @Autowired
    private JobService jobService;

    @GetMapping
    public ResponseEntity<PageResponse<JobResponse>> getAllJobs(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
                                                            Pageable pageable) {
        PageResponse<JobResponse> list = jobService.getAllJobs(pageable);
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/filter")
    public ResponseEntity<PageResponse<JobResponse>> filterJobs(JobFilterRequest filter,
                                                                @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(jobService.filterJobs(filter, pageable));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<JobResponse> findJobById(@PathVariable Integer id) {
        Job job = jobService.findJobById(id);
        return ResponseEntity.ok(new JobResponse(job));
    }

    @GetMapping("/skill/{skillSlug}")
    public ResponseEntity<PageResponse<JobResponse>> findJobBySkillSlug(@PathVariable String skillSlug, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
    Pageable pageable) {
        PageResponse<JobResponse> list = jobService.findJobsBySkillSlug(skillSlug, pageable);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/domain/{domainSlug}")
    public ResponseEntity<PageResponse<JobResponse>> findJobByDomainSlug(@PathVariable String domainSlug, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
    Pageable pageable) {
        PageResponse<JobResponse> list = jobService.findJobsByDomainSlug(domainSlug, pageable);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<JobResponse> findJobBySlug(@PathVariable String slug) {
         Job job = jobService.findJobBySlug(slug);
        return ResponseEntity.ok(new JobResponse(job));
    }

    @PreAuthorize("hasRole('RECRUIT')")
    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody JobRequest jobRequest, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        jobService.saveJob(jobRequest, userDetailsCustom.getUserEntity());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RECRUIT')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@RequestBody JobRequest jobRequest, @PathVariable Integer id, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        jobService.updateJob(jobRequest, id, userDetailsCustom.getUserEntity());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('RECRUIT','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Integer id, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        jobService.deleteJob(id, userDetailsCustom.getUserEntity());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<PageResponse<JobResponse>> findJobByTitle(@PathVariable String title, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        PageResponse<JobResponse> jobs = jobService.findJobByTitleAndStatus(title, pageable);
        return ResponseEntity.ok(jobs);
    }
}
