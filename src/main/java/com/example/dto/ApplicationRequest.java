package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class ApplicationRequest {
    @Size(max = 500, message = "CV url tối đa 500 ký tự")
    private String cvUrl;

    @NotBlank(message = "Cover letter không được để trống")
    private String coverLetter;

    private MultipartFile cvFile;

    @NotNull(message = "Job id không được để trống")
    private Integer jobId;
}
