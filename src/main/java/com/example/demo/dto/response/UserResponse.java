package com.example.demo.dto.response;

import com.example.demo.model.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
public class UserResponse {
    private String username;
    private String roleName;
    private UUID id;
}
