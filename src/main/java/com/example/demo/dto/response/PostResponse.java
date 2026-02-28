package com.example.demo.dto.response;


import com.example.demo.constant.UrgencyLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
public class PostResponse {
    private UUID id;
    private String title;
    private String username;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private Date createdAt;
    private boolean isViewed;
    private String description;

    private String contactInfo;
    private String email;
    private String phoneNumber;
    private UUID serviceId;
    private String serviceName;
    private String subService;
    private UrgencyLevel urgencyLevel;
}