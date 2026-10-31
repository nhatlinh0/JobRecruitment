package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequest {
    @NotBlank(message = "Current Password không được để trống")
    @JsonProperty("current_password")
    private String currentPassword;

    @NotBlank(message = "New Password không được để trống")
    @JsonProperty("new_password")
    private String newPassword;

    @NotBlank(message = "Confirm Password không được để trống")
    @JsonProperty("confirm_password")
    private String confirmPassword;
}
