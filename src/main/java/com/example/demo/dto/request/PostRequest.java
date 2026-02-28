package com.example.demo.dto.request;

import com.example.demo.constant.UrgencyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Contact info is required")
    private String contactInfo;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Service ID is required")
    private UUID serviceId;

    @NotBlank(message = "Sub service is required")
    private String subService;

    @NotNull(message = "Urgency level is required")
    private UrgencyLevel urgencyLevel;
}