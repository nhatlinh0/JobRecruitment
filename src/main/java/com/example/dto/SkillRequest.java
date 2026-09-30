package com.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillRequest {
    @Size(max = 100, message = "Name tối đa 100 ký tự")
    private String name;

    @NotNull(message = "Domain ID không được để trống")
    private Integer domainId;
}
