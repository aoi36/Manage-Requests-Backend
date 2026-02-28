package com.example.demo.mapper;

import com.example.demo.dto.response.PostResponse;
import com.example.demo.dto.response.PostSummaryResponse;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PostMapper {
    private final UserRepository userRepository;

    public PostResponse toPostResponse(Post post){
        if (post == null) return null;

        Optional<User> user = userRepository.findByUsername(post.getUser().getUsername());

        if (user.isEmpty()){
            return null;
        }

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .username(post.getUser().getUsername())
                .createdAt(post.getCreatedAt())
                .fileName(post.getFileName())
                .filePath(post.getFilePath())
                .fileSize(post.getFileSize())
                .isViewed(post.isViewed())
                .contactInfo(post.getContactInfo())
                .email(post.getEmail())
                .phoneNumber(post.getPhoneNumber())
                .serviceId(post.getService().getId())
                .serviceName(post.getService().getName())
                .subService(post.getSubService())
                .urgencyLevel(post.getUrgencyLevel())
                .build();
    }

    public PostSummaryResponse toPostSummaryResponse(Post post) {
        if (post == null) return null;

        return PostSummaryResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .username(post.getUser().getUsername())
                .fileName(post.getFileName())
                .isViewed(post.isViewed())
                .build();
    }
}