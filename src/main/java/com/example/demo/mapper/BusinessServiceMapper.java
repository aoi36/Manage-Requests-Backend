package com.example.demo.mapper;

import com.example.demo.dto.request.BusinessServiceRequest;
import com.example.demo.dto.response.BusinessServiceResponse;
import com.example.demo.model.BusinessService;
import org.springframework.stereotype.Component;

@Component
public class BusinessServiceMapper {

    public BusinessServiceResponse toResponse(BusinessService businessService) {
        if (businessService == null) return null;

        return BusinessServiceResponse.builder()
                .id(businessService.getId())
                .name(businessService.getName())
                .displayOrder(businessService.getDisplayOrder())
                .build();
    }

    public BusinessService toEntity(BusinessServiceRequest request) {
        if (request == null) return null;

        return BusinessService.builder()
                .name(request.getName())
                .displayOrder(request.getDisplayOrder())
                .build();
    }
}