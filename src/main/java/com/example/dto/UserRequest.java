package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class UserRequest {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, message = "Password tối thiểu 8 ký tự")
    private String password;

    @NotBlank(message = "Username không được để trống")
    private String username;

    @NotNull(message = "Role ID không được để trống")
    private Integer roleId;
}
