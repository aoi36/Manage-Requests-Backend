package com.example.demo.service.interfaces;

import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.UserDetailResponse;
import com.example.demo.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse updateUser(UUID id, UserRequest userRequest);
    PageResponse<?> searchUsersByName(String name, int pageNumber, int pageSize, String sortBy, String sortDirection);
    UserDetailResponse getUserDetail(UUID id);
}
