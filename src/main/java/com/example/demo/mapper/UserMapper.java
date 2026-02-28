package com.example.demo.mapper;

import com.example.demo.dto.response.PostResponse;
import com.example.demo.dto.response.UserDetailResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roleName(user.getRole() != null
                        ? user.getRole().getName().name()
                        : null)
                .build();
    }

    public UserDetailResponse toUserDetailResponse(User user){
        if (user == null){
            return null;
        }

        Set<PostResponse> postResponses = null;

        if (user.getPosts() != null) {
            postResponses = user.getPosts().stream()
                    .map(this::toPostResponse)
                    .collect(Collectors.toSet());
        }

            return UserDetailResponse.builder()
                    .username(user.getUsername())
                    .id(user.getId())
                    .roleName(user.getRole().getName().name())
                    .createdDate(user.getCreatedAt())
                    .lastModifiedDate(user.getUpdatedAt())
                    .posts(postResponses)
                    .build();
    }

    private PostResponse toPostResponse(Post post) {
        if (post == null) {
            return null;
        }

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .username(
                        post.getUser() != null
                                ? post.getUser().getUsername()
                                : null
                )
                .fileName(post.getFileName())
                .filePath(post.getFilePath())
                .fileSize(post.getFileSize())
                .createdAt(post.getCreatedAt())
                .isViewed(post.isViewed())
                .description(post.getDescription())
                .contactInfo(post.getContactInfo())
                .email(post.getEmail())
                .phoneNumber(post.getPhoneNumber())
                .subService(post.getSubService())
                .urgencyLevel(post.getUrgencyLevel())
                .serviceId(
                        post.getService() != null
                                ? post.getService().getId()
                                : null
                )
                .serviceName(
                        post.getService() != null
                                ? post.getService().getName()
                                : null
                )
                .build();
    }
}
