package com.example.demo.dto.request;

import com.example.demo.constant.RoleName;
import com.example.demo.model.Role;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String username;
    private String password;
    private RoleName role;
}
