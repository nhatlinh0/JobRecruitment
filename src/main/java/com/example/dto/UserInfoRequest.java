package com.example.dto;

import com.example.enums.WorkingType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
public class UserInfoRequest {
    private Integer salaryMin;

    private Integer salaryMax;

    @NotNull(message = "Trạng thái thương lượng lương không được để trống")
    private boolean salaryNegotiable;

    @Size(max = 50, message = "Work area tối đa 50 ký tự")
    private String city;

    private Integer experienceMin;

    private Integer experienceMax;

    private WorkingType workingType;

    private MultipartFile image;

    private Integer domainId ;

    @Size(max = 50, message = "Username tối đa 50 ký tự")
    private String username;

    @Size(max = 10, message = "Tối đa 10 skills")
    private Set<Integer> skillIds;
}
