package com.example.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(min = 8, message = "Username must be at least 8 characters")
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Size(min = 8, message = "Retype password must be at least 8 characters")
    private String confirmPassword;
}