package com.example.dto;

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
    @Size(max = 50, message = "Salary tối đa 50 ký tự")
    private String salary;

    @Size(max = 50, message = "Work area tối đa 50 ký tự")
    private String workArea;

    @Size(max = 50, message = "Exp tối đa 50 ký tự")
    private String exp;

    @Size(max = 100, message = "Image URL tối đa 100 ký tự")
    private String image;

    private Integer domainId ;

    @Size(max = 50, message = "Username tối đa 50 ký tự")
    private String username;
}
