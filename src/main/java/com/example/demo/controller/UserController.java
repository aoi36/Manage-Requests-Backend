package com.example.demo.controller;

import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "User updated successfully", userService.updateUser(id, userRequest))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserDetail(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "User details fetched successfully", userService.getUserDetail(id))
        );
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> searchUsers(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Users fetched successfully",
                        userService.searchUsersByName(name, page, size, sortBy, sortDirection))
        );
    }
}