package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class PostSummaryResponse {
    private UUID id;
    private String title;
    private String username;
    private String fileName;
    private Date createdAt;
    private boolean isViewed;
}