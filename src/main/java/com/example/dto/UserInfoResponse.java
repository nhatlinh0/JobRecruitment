package com.example.dto;
import com.example.entity.Skill;
import com.example.entity.UserInfo;
import com.example.enums.WorkingType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter

public class UserInfoResponse {
    private Integer salaryMax;
    private Integer salaryMin;
    private boolean salaryNegotiable;
    private String city;
    private Integer experienceMax;
    private Integer experienceMin;
    private WorkingType workType;
    private String image;
    private String domainName;
    private String username;
    private Set<String> skills;

    public UserInfoResponse (UserInfo userInfo) {
        if(userInfo != null) {
            this.salaryMax = userInfo.getSalaryMax();
            this.salaryMin = userInfo.getSalaryMin();
            this.salaryNegotiable = userInfo.isSalaryNegotiable();
            this.city = userInfo.getCity();
            this.experienceMax = userInfo.getExperienceMax();
            this.experienceMin = userInfo.getExperienceMin();
            this.workType = userInfo.getWorkingType();
            this.image = userInfo.getImage();
            this.domainName = userInfo.getDomain().getName();
            this.username = userInfo.getUsername();
            this.skills = userInfo.getSkills().stream().map(Skill::getName).collect(Collectors.toSet());
        }
    }
}
