package com.example.demo.service.interfaces;



import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {

    TokenResponse login(LoginRequest loginRequest, HttpServletResponse response);

    void register(UserRequest registerRequest);

    TokenResponse refresh(HttpServletRequest request, HttpServletResponse response);

    void logout(HttpServletRequest request, HttpServletResponse response);
}