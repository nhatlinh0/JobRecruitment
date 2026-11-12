package com.example.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PackageRequest {

    @NotBlank(message = "Name không được để trống")
    @Size(max = 100, message = "Name tối đa 100 ký tự")
    private String name;

    @NotNull(message = "Price không được để trống")
    @DecimalMin(value = "1", message = "Price phải lớn hơn hoặc bằng 1 VND")
    @Digits(integer = 8, fraction = 2, message = "Price tối đa 8 chữ số nguyên và 2 chữ số thập phân")
    private BigDecimal price;

    @NotNull(message = "Credits không được để trống")
    @Min(value = 0, message = "Credits không được âm")
    private Integer credits;

    @Size(max = 255, message = "Description tối đa 255 ký tự")
    private String description;
}