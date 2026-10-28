package com.example.service;

import com.example.dto.JobRequest;
import com.example.dto.JobResponse;
import com.example.dto.PageResponse;
import com.example.dto.UserInfoRequest;
import com.example.entity.*;
import com.example.enums.Status;
import com.example.enums.UserStatus;
import com.example.repository.*;
import com.example.security.UserDetailsCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashSet;
import java.util.List;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserRepository userRepository;

    public PageResponse<JobResponse> getAllJobs(Pageable pageable) {
       return PageResponse.from(jobRepository.findAll(pageable).map(JobResponse::new));
    }

    public void saveJob(JobRequest jobRequest, User user) {
        validateJob(jobRequest);

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated() ||
//                !(authentication.getPrincipal() instanceof UserDetailsCustom userDetailsCustom)) {
//            throw new RuntimeException("Vui lòng đăng nhập!");
//        }
//        User user = userDetailsCustom.getUserEntity();

        Company company = user.getCompany();
//        Company company = companyRepository.findById(jobRequest.getCompanyId()).orElseThrow(() -> new RuntimeException("Không tìm thấy company"));
        Domain domain = domainRepository.findById(jobRequest.getDomainId()).orElseThrow(() -> new RuntimeException("Không tìm thấy domain"));

        Job job = new Job();
        job.setTitle(jobRequest.getTitle());
        job.setSlug(jobRequest.getSlug());
        job.setCompany(company);
        job.setAddress(jobRequest.getAddress());
        job.setEndDate(jobRequest.getEndDate());
        job.setWorkingType(jobRequest.getWorkingType());
        job.setDescription(jobRequest.getDescription());
        job.setBenefits(jobRequest.getBenefits());
        job.setRequirements(jobRequest.getRequirements());
        job.setDomain(domain);
        job.setExperienceMin(jobRequest.getExperienceMin());
        job.setExperienceMax(jobRequest.getExperienceMax());
        job.setSalaryMin(jobRequest.getSalaryMin());
        job.setSalaryMax(jobRequest.getSalaryMax());
        job.setSalaryNegotiable(jobRequest.getSalaryNegotiable());
        job.setCity(jobRequest.getCity());
        job.setPrioritize(jobRequest.getPrioritize());

        List<Skill> newSkills = skillRepository.findAllById(jobRequest.getSkillIds());
        job.setSkills(new HashSet<>(newSkills));

        jobRepository.save(job);
    }

    public void updateJob(JobRequest jobRequest, Integer jobId, User user) {
        validateJob(jobRequest);

        Domain domain = domainRepository.findById(jobRequest.getDomainId()).orElseThrow(() -> new RuntimeException("Không tìm thấy domain"));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Không tìm thấy job"));

        Company company = user.getCompany();
        if (!company.getId().equals(job.getCompany().getId())) {
            throw new RuntimeException("Không có quyền truy cập");
        }

        job.setTitle(jobRequest.getTitle());
        job.setSlug(jobRequest.getSlug());
        job.setAddress(jobRequest.getAddress());
        job.setEndDate(jobRequest.getEndDate());
        job.setWorkingType(jobRequest.getWorkingType());
        job.setDescription(jobRequest.getDescription());
        job.setBenefits(jobRequest.getBenefits());
        job.setRequirements(jobRequest.getRequirements());
        job.setDomain(domain);
        job.setExperienceMin(jobRequest.getExperienceMin());
        job.setExperienceMax(jobRequest.getExperienceMax());
        job.setSalaryMin(jobRequest.getSalaryMin());
        job.setSalaryMax(jobRequest.getSalaryMax());
        job.setSalaryNegotiable(jobRequest.getSalaryNegotiable());
        job.setCity(jobRequest.getCity());
        job.setPrioritize(jobRequest.getPrioritize());

        List<Skill> newSkills = skillRepository.findAllById(jobRequest.getSkillIds());
        job.setSkills(new HashSet<>(newSkills));

        jobRepository.save(job);
    }

    public void deleteJob(Integer id, User user) {
        Company company = user.getCompany();
        Job job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy job"));

        if (!company.getId().equals(job.getCompany().getId())) {
            throw new RuntimeException("Không có quyền truy cập");
        }
        jobRepository.deleteById(id);
    }

    public Job findJobById(Integer id) {
        return jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy Job"));
    }

    public Job findJobBySlug(String slug) {
        return jobRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("Không tìm thấy Job"));
    }

    public PageResponse<JobResponse> findJobsBySkillSlug(String slug, Pageable pageable) {
        Page<Job> list = jobRepository.findBySkillsSlug(slug, pageable);
        return PageResponse.from(list.map(JobResponse::new));
    }

    public PageResponse<JobResponse> findJobsByDomainSlug(String slug, Pageable pageable) {
        Page<Job> list = jobRepository.findByDomainSlug(slug, pageable);
        return PageResponse.from(list.map(JobResponse::new));
    }

    public PageResponse<JobResponse> findJobsByCompanySlug(String companySlug, Pageable pageable) {
        Page<Job> list = jobRepository.findByCompanySlug(companySlug, pageable);
        return PageResponse.from(list.map(JobResponse::new));
    }

    public List<JobResponse> findAllJobsByCompany(User user) {
        Company company = user.getCompany();
        List<Job> list = jobRepository.findAllByCompanyId(company.getId());
        return list.stream().map(JobResponse::new).toList();
    }

    public Job acceptJob(Integer jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(()->new RuntimeException("Job không tồn tại"));
        job.setStatus(Status.ACCEPTED);
        return jobRepository.save(job);
    }

    public Job rejectJob(Integer jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(()->new RuntimeException("Job không tồn tại"));
        job.setStatus(Status.REJECT);
        return jobRepository.save(job);
    }

    public Job suspendJob(Integer jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(()->new RuntimeException("Job không tồn tại"));
        job.setStatus(Status.SUSPEND);
        return jobRepository.save(job);
    }

    public void validateJob(JobRequest jobRequest) {
        boolean isNegotiable = jobRequest.getSalaryNegotiable();
        if (isNegotiable) {
            if (jobRequest.getSalaryMin() != null || jobRequest.getSalaryMax() != null) {
                throw new IllegalArgumentException("Khi chọn thỏa thuận lương, không được nhập Lương tối thiểu và Lương tối đa.");
            }
        } else if (!isNegotiable) {
            if (jobRequest.getSalaryMin() == null || jobRequest.getSalaryMax() == null) {
                throw new IllegalArgumentException("Vui lòng nhập đầy đủ Lương tối thiểu và Lương tối đa.");
            }

            if (jobRequest.getSalaryMin() > jobRequest.getSalaryMax()) {
                throw new IllegalArgumentException("Lương tối thiểu không được lớn hơn Lương tối đa.");
            }
        }
        if (jobRequest.getExperienceMin() > jobRequest.getExperienceMax()) {
            throw new IllegalArgumentException("Kinh nghiệm tối thiểu không được lớn hơn kinh nghiệm tối đa.");
        }
    }
}
