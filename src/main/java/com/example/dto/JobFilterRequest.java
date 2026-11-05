package com.example.dto;

import com.example.enums.WorkingType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobFilterRequest {
    private List<WorkingType> workingTypes;
    private List<String> domainSlugs;
    private List<String> skillSlugs;
    private String city;
    private Integer salaryMin;
    private Integer salaryMax;
    private Integer experienceMin;
    private Integer experienceMax;
}
