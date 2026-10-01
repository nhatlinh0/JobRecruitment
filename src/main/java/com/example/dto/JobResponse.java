package com.example.dto;

import com.example.entity.Job;
import com.example.entity.Skill;
import com.example.enums.Status;
import com.example.enums.WorkingType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class JobResponse {
    private Integer id;
    private String title;
    private String companyName;
    private String address;
    private String endDate;
    private WorkingType workingType;
    private String description;
    private String requirements;
    private String benefits;
    private String createAt;
    private String domainName;
    private Integer experienceMin;
    private Integer experienceMax;
    private Integer salaryMin;
    private Integer salaryMax;
    private Boolean salaryNegotiable;
    private String city;
    private Status status;
    private Set<String> skills;

    public JobResponse(Job job) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.companyName = job.getCompany().getName();
        this.address = job.getAddress();
        this.endDate = job.getEndDate().toString();
        this.workingType = job.getWorkingType();
        this.description = job.getDescription();
        this.requirements = job.getRequirements();
        this.benefits = job.getBenefits();
        this.createAt = job.getCreatedAt().toString();
        this.domainName = job.getDomain().getName();
        this.experienceMin = job.getExperienceMin();
        this.experienceMax = job.getExperienceMin();
        this.salaryMin = job.getSalaryMin();
        this.salaryMax = job.getSalaryMax();
        this.salaryNegotiable = job.getSalaryNegotiable();
        this.city = job.getCity();
        this.status = job.getStatus();
        this.skills = job.getSkills().stream().map(Skill::getName).collect(Collectors.toSet());
    }
}
