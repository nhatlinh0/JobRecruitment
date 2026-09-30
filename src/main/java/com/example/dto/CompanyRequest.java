package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequest {
    @NotBlank(message = "Name không được để trống")
    @Size(max = 255, message = "Name tối đa 255 ký tự")
    private String name;

    @NotBlank(message = "Address không được để trống")
    @Size(max = 500, message = "Address tối đa 500 ký tự")
    private String address;

    @NotBlank(message = "Company size không được để trống")
    @Size(max = 50, message = "Company size tối đa 50 ký tự")
    private String companySize;

    @NotBlank(message = "Phone không được để trống")
    @Size(max = 20, message = "Phone tối đa 20 ký tự")
    private String phone;

    @NotBlank(message = "Logo url không được để trống")
    @Size(max = 500, message = "Logo url tối đa 500 ký tự")
    private String logoUrl;

    @NotBlank(message = "Profile url không được để trống")
    @Size(max = 500, message = "Profile url tối đa 500 ký tự")
    private String profilePDFUrl;

//    @NotBlank(message = "Status không được để trống")
//    @Size(max = 30, message = "Status tối đa 30 ký tự")
//    private String status;

    @NotBlank(message = "Country không được để trống")
    @Size(max = 50, message = "County tối đa 50 ký tự")
    private String country;

    @NotNull(message = "Industry id không được để trống")
    private Integer industryId;
}
