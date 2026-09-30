package com.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndustryRequest {
    @Size(max = 100, message = "Name tối đa 100 ký tự")
    private String name;

    @Size(max = 50, message = "Description tối đa 50 ký tự")
    private String description;
}
