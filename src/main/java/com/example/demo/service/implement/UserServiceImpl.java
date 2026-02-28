package com.example.demo.service.implement;

import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.UserDetailResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.interfaces.UserService;
import com.example.demo.utils.SortUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SortUtils sortUtils;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserDetailResponse getUserDetail(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));


        return userMapper.toUserDetailResponse(user);
    }
    @Override
    @Transactional
    public UserResponse updateUser(UUID id, UserRequest userRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        if (!existingUser.getUsername().equals(userRequest.getUsername()) &&
                userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username has been taken");
        }

        Role role = roleRepository.findByName(userRequest.getRole())
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        if (userRequest.getPassword() != null){
            existingUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        existingUser.setUsername(userRequest.getUsername());
        existingUser.setRole(role);

        User updatedUser = userRepository.save(existingUser);

        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public PageResponse<?> searchUsersByName(String name, int pageNumber, int pageSize, String sortBy, String sortDirection) {
        if (pageNumber < 1) {
            pageNumber = 1;
        }

        Sort sort = sortUtils.buildSort(sortBy, sortDirection);

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<User> userPage = userRepository.findByUsernameContainingIgnoreCase(name, pageable);

        var userResponses = userPage.getContent().stream()
                .map(userMapper::toUserResponse)
                .toList();

        return PageResponse.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .content(userResponses)
                .build();
    }
}
