package com.example.dto;

import com.example.entity.Company;
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
    private String slug;
    private CompanyDtoCustom company;
    private String address;
    private String endDate;
    private WorkingType workingType;
    private String description;
    private String requirements;
    private String benefits;
    private String createAt;
    private DomainDtoCustom domain;
    private Integer experienceMin;
    private Integer experienceMax;
    private Integer salaryMin;
    private Integer salaryMax;
    private Boolean salaryNegotiable;
    private String city;
    private Status status;
    private Set<SkillDtoCustom> skills;
    private Boolean prioritize;

    public JobResponse(Job job) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.slug = job.getSlug();
//        this.companyName = job.getCompany().getName();
        this.address = job.getAddress();
        this.endDate = job.getEndDate().toString();
        this.workingType = job.getWorkingType();
        this.description = job.getDescription();
        this.requirements = job.getRequirements();
        this.benefits = job.getBenefits();
        this.createAt = job.getCreatedAt().toString();
//        this.domain = job.getDomain().getName();
        this.experienceMin = job.getExperienceMin();
        this.experienceMax = job.getExperienceMax();
        this.salaryMin = job.getSalaryMin();
        this.salaryMax = job.getSalaryMax();
        this.salaryNegotiable = job.getSalaryNegotiable();
        this.city = job.getCity();
        this.status = job.getStatus();
//        this.skills = job.getSkills().stream().map(Skill::getName).collect(Collectors.toSet());
        this.prioritize = job.getPrioritize();

        if (job.getDomain() != null) {
            this.domain = new JobResponse.DomainDtoCustom(
                    job.getDomain().getId(),
                    job.getDomain().getName()
            );
        }

        if (job.getSkills() != null) {
            this.skills = job.getSkills().stream()
                    .map(skill -> new JobResponse.SkillDtoCustom(skill.getId(), skill.getName()))
                    .collect(Collectors.toSet());
        }

        if (job.getCompany() != null) {
            this.company = new JobResponse.CompanyDtoCustom(
                    job.getCompany().getId(),
                    job.getCompany().getName(),
                    job.getCompany().getLogoUrl(),
                    job.getCompany().getCompanySize(),
                    job.getCompany().getCountry()
            );
        }
    }

    @Getter
    @Setter
    public static class DomainDtoCustom {
        private Integer id;
        private String name;

        public DomainDtoCustom(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Getter
    @Setter
    public static class SkillDtoCustom {
        private Integer id;
        private String name;

        public SkillDtoCustom(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Getter
    @Setter
    public static class CompanyDtoCustom {
        private Integer id;
        private String name;
        private String logoUrl;
        private String companySize;
        private String country;

        public CompanyDtoCustom(Integer id, String name, String logoUrl, String companySize, String country) {
            this.id = id;
            this.name = name;
            this.logoUrl = logoUrl;
            this.companySize = companySize;
            this.country = country;
        }
    }
}
