package com.example.demo.service.implement;

import com.example.demo.constant.RoleName;
import com.example.demo.constant.TokenType;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.exception.DataInUseException;
import com.example.demo.exception.InvalidDataException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import com.example.demo.service.JwtService;
import com.example.demo.service.interfaces.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final int ACCESS_TOKEN_COOKIE_EXPIRATION = 60 * 10; // 10 minutes
    private static final int REFRESH_TOKEN_COOKIE_EXPIRATION = 60 * 60 * 24 * 14; // 14 days

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + loginRequest.getUsername()));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        setTokenCookies(response, accessToken, refreshToken);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    @Transactional
    public void register(UserRequest registerRequest) {

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already in use");
        }

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Role 'USER' not found"));

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(userRole)
                .build();

        userRepository.save(user);
    }

    @Override
    public TokenResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = "";

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            refreshToken = Arrays.stream(cookies)
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse("");
        }

        if (refreshToken.isBlank()) {
            throw new InvalidDataException("Refresh token is missing");
        }

        final String username = jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!jwtService.isValidToken(refreshToken, TokenType.REFRESH_TOKEN, user)) {
            throw new InvalidDataException("Invalid Refresh Token");
        }

        String accessToken = jwtService.generateAccessToken(user);

        Cookie accessTokenCookie = createCookie("accessToken", accessToken, ACCESS_TOKEN_COOKIE_EXPIRATION);
        response.addCookie(accessTokenCookie);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie accessTokenCookie = createCookie("accessToken", "", 0);
        Cookie refreshTokenCookie = createCookie("refreshToken", "", 0);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        SecurityContextHolder.clearContext();
    }


    private void setTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        response.addCookie(createCookie("accessToken", accessToken, ACCESS_TOKEN_COOKIE_EXPIRATION));
        response.addCookie(createCookie("refreshToken", refreshToken, REFRESH_TOKEN_COOKIE_EXPIRATION));
    }

    private Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Strict");
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}