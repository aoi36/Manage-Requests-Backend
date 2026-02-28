package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.intellij.lang.annotations.Identifier;
import jakarta.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Identifier
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}