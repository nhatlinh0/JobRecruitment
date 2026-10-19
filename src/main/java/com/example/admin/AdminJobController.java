package com.example.admin;

import com.example.dto.JobResponse;
import com.example.entity.Job;
import com.example.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/jobs")
public class AdminJobController {
    @Autowired
    private JobService jobService;

    @PutMapping("/{jobId}/accept")
    public ResponseEntity<JobResponse> acceptJob(@PathVariable Integer jobId) {
        Job job = jobService.acceptJob(jobId);
        return ResponseEntity.ok(new JobResponse(job));
    }

    @PutMapping("/{jobId}/reject")
    public ResponseEntity<JobResponse> rejectJob(@PathVariable Integer jobId) {
        Job job = jobService.rejectJob(jobId);
        return ResponseEntity.ok(new JobResponse(job));
    }

    @PutMapping("/{jobId}/suspend")
    public ResponseEntity<JobResponse> suspendJob(@PathVariable Integer jobId) {
        Job job = jobService.suspendJob(jobId);
        return ResponseEntity.ok(new JobResponse(job));
    }
}
