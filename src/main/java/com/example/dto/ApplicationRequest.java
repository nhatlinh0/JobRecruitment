package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ApplicationRequest {
    @NotBlank(message = "CV url không được để trống")
    @Size(max = 500, message = "CV url tối đa 500 ký tự")
    private String cvUrl;

    @NotBlank(message = "Cover letter không được để trống")
    private String coverLetter;

    @NotNull(message = "Job id không được để trống")
    @JsonProperty(value = "job_id")
    private Integer jobId;
}
