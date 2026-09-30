package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DomainRequest {
    @NotBlank(message = "Name không được để trống")
    @Size(max = 100, message = "Name tối đa 100 ký tự")
    private String name;

    @Size(max = 100, message = "Slug tối đa 100 ký tự")
    @NotBlank(message = "Slug không được để trống")
    private String slug;
}
