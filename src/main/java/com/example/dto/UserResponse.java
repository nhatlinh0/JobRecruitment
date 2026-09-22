package com.example.dto;

import com.example.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserResponse {
    private int id;
    private String email;
    private String username;
    private String roleName;

    public UserResponse (User user) {
        if(user != null) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.username = user.getUsername();
            this.roleName = user.getRole().getName();
        }
    }
}
