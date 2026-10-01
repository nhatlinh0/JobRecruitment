package com.example.dto;

import com.example.enums.WorkingType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class JobRequest {
    @NotBlank(message = "Title không được để trống")
    @Size(max = 255, message = "Title không quá 255 ký tự")
    private String title;

    @NotNull(message = "Company id không được để trống")
    @JsonProperty("company_id")
    private Integer companyId;

    @Size(max = 255, message = "Address không quá 255 ký tự")
    private String address;

    @NotNull(message = "End date không được để trống")
    @JsonProperty("end_date")
    private LocalDate endDate;

    @NotNull(message = "Working type không được để trống")
    @Size(max = 50, message = "Working type không quá 50 ký tự")
    @JsonProperty("working_type")
    private WorkingType workingType;

    private String description;

    private String requirements;

    private String benefits;

    @JsonProperty("domain_id")
    private Integer domainId;

    @NotNull(message = "Experience min không được để trống")
    @JsonProperty("experience_min")
    private Integer experienceMin;

    @NotNull(message = "Experience max không được để trống")
    @JsonProperty("experience_max")
    private Integer experienceMax;

    @JsonProperty("salary_min")
    private Integer salaryMin;

    @JsonProperty("salary_max")
    private Integer salaryMax;

    @NotNull(message = "Salary negotiable không được để trống")
    @JsonProperty("salary_negotiable")
    private Boolean salaryNegotiable;

    @NotBlank(message = "City không được để trống")
    @Size(max = 50, message = "City không quá 50 ký tự")
    private String city;

    @NotEmpty
    @Size(max = 6, message = "Job tối đa 6 skills")
    @JsonProperty("skill_ids")
    private Set<Integer> skillIds;
}
