package com.example.service;

import com.example.dto.ApplicationRequest;
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
        User owner = job.getCompany().getUser().stream().filter((x) -> x.getRole().getName().equals("RECRUIT")).findFirst().orElseThrow(() -> new RuntimeException("Chỉ dành cho nhà tuyển dụng!"));

        if (!owner.getId().equals(user.getId())) {
            throw new RuntimeException("Không có quyền truy cập!");
        }

        List<ApplicationResponse> applicationResponseList = job.getApplications().stream().map((x) -> new ApplicationResponse(x)).toList();
        return applicationResponseList;
    }

    public List<ApplicationResponse> getAllApplicationsByUser(User user) {
        List<ApplicationResponse> applicationResponseList = applicationRepository.findAllByUser(user).stream().map((x) -> new ApplicationResponse(x)).toList();
        return applicationResponseList;
    }

    public void saveApplication(ApplicationRequest applicationRequest, User user) {
        Job job = jobRepository.findById(applicationRequest.getJobId()).orElseThrow(() -> new RuntimeException("Không tìm thấy job"));

        Application application = new Application();
        application.setCvUrl(applicationRequest.getCvUrl());
        application.setCoverLetter(applicationRequest.getCoverLetter());
        application.setUser(user);
        application.setJob(job);

        applicationRepository.save(application);
    }

    public void updateApplication(ApplicationRequest applicationRequest, Integer id, User user) {
        Application application = user.getApplications().stream().filter((x) -> x.getId().equals(id)).findFirst().orElseThrow(() -> new RuntimeException("Công ty này chưa có nhà tuyển dụng nào!"));

        application.setCvUrl(applicationRequest.getCvUrl());
        application.setCoverLetter(applicationRequest.getCoverLetter());

        applicationRepository.save(application);
    }

    public void deleteApplication( Integer id) {
        applicationRepository.deleteById(id);
    }
}
