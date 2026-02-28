package com.example.demo.controller;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.service.interfaces.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest registerRequest) {
        authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(201,
                        "User registered successfully", null
                        )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest,
                                   @NonNull HttpServletResponse response) {
        return ResponseEntity.ok(
                new ApiResponse<>(200,
                        "Login success",
                        authenticationService.login(loginRequest, response))
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response) {
        return ResponseEntity.ok(
                new ApiResponse<>(200,
                        "Token refreshed successfully",
                        authenticationService.refresh(request, response))
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response) {
        authenticationService.logout(request, response);

        return ResponseEntity.ok(
                new ApiResponse<>(200,
                        "Logout success",
                        null)
        );
    }
}