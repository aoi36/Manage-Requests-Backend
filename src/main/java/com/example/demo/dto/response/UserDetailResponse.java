package com.example.demo.dto.response;

import com.example.demo.model.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
public class UserDetailResponse {
    private UUID id;
    private String username;
    private String roleName;
    private Set<PostResponse> posts;
    private Date createdDate;
    private Date lastModifiedDate;

}