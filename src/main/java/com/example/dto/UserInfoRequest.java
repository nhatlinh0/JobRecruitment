package com.example.dto;

import com.example.enums.WorkingType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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

    @Size(max = 100, message = "Image URL tối đa 100 ký tự")
    private String image;

    private Integer domainId ;

    @Size(max = 50, message = "Username tối đa 50 ký tự")
    private String username;
}
