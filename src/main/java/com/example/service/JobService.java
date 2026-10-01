package com.example.service;

import com.example.dto.JobRequest;
import com.example.dto.JobResponse;
import com.example.dto.UserInfoRequest;
import com.example.entity.Company;
import com.example.entity.Domain;
import com.example.entity.Job;
import com.example.entity.Skill;
import com.example.repository.CompanyRepository;
import com.example.repository.DomainRepository;
import com.example.repository.JobRepository;
import com.example.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<JobResponse> getAllJobs() {
       return jobRepository.findAll().stream().map(x -> new JobResponse(x)).toList();
    }

    public void saveJob(JobRequest jobRequest) {
        validateJob(jobRequest);

        Company company = companyRepository.findById(jobRequest.getCompanyId()).orElseThrow(() -> new RuntimeException("Không tìm thấy company"));
        Domain domain = domainRepository.findById(jobRequest.getDomainId()).orElseThrow(() -> new RuntimeException("Không tìm thấy domain"));


        Job job = new Job();
        job.setTitle(jobRequest.getTitle());
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
        for (Integer id : jobRequest.getSkillIds()) {
            Skill skill = skillRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy skill"));
            job.getSkills().add(skill);
        }

        jobRepository.save(job);
    }

    public void updateJob(JobRequest jobRequest, Integer jobId) {
        validateJob(jobRequest);

        Domain domain = domainRepository.findById(jobRequest.getDomainId()).orElseThrow(() -> new RuntimeException("Không tìm thấy domain"));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Không tìm thấy job"));

        job.setTitle(jobRequest.getTitle());
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
        for (Integer id : jobRequest.getSkillIds()) {
            Skill skill = skillRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy skill"));
            job.getSkills().add(skill);
        }

        jobRepository.save(job);
    }

    public void deleteJob(Integer id) {
        jobRepository.deleteById(id);
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
