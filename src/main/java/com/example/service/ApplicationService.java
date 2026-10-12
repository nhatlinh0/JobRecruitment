package com.example.service;

import com.example.dto.ApplicationResponse;
import com.example.entity.Application;
import com.example.entity.Job;
import com.example.entity.User;
import com.example.repository.ApplicationRepository;
import com.example.repository.JobRepository;
import com.example.security.UserDetailsCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

//    ADMIN
    public List<ApplicationResponse> getAllApplications() {
        List<ApplicationResponse> list = applicationRepository.findAll().stream().map((x) -> new ApplicationResponse(x)).toList();
        return list;
    }

    public ApplicationResponse getApplicationById (Integer id) {
        Application application = applicationRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy Application"));
        return new ApplicationResponse(application);
    }

    public List<ApplicationResponse> getAllApplicationsByJob(Integer jobId, User user) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Không tìm thấy job"));
        User owner = job.getCompany().getUser().stream().filter((x) -> x.getRole().getName().equals("RECRUITER")).findFirst().orElseThrow(() -> new RuntimeException("Công ty này chưa có nhà tuyển dụng nào!"));

        if (!owner.equals(user)) {
            throw new RuntimeException("Không có quyền truy cập");
        }

        List<ApplicationResponse> applicationResponseList = job.getApplications().stream().map((x) -> new ApplicationResponse(x)).toList();
        return applicationResponseList;
    }

    public List<ApplicationResponse> getAllApplicationsByUser(User user) {

        List<ApplicationResponse> applicationResponseList = user.getApplications().stream().map((x) -> new ApplicationResponse(x)).toList();
        return applicationResponseList;
    }
}
