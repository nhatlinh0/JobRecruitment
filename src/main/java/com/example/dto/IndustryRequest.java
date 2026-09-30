package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndustryRequest {
    @NotBlank(message = "Name không được để trống")
    @Size(max = 100, message = "Name tối đa 100 ký tự")
    private String name;

    @NotBlank(message = "Description không được để trống")
    @Size(max = 200, message = "Description tối đa 200 ký tự")
    private String description;
}
