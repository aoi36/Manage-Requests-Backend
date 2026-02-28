package com.example.demo.service.interfaces;

import com.example.demo.dto.request.BusinessServiceRequest;
import com.example.demo.dto.response.BusinessServiceResponse;
import com.example.demo.dto.response.PageResponse;

import java.util.List;
import java.util.UUID;

public interface BusinessServiceService {
    BusinessServiceResponse createService(BusinessServiceRequest request);
    PageResponse<?> getAllServices(int pageNumber, int pageSize, String sortBy, String sortDirection);
    BusinessServiceResponse getServiceById(UUID id);
    BusinessServiceResponse updateService(UUID id, BusinessServiceRequest request);
    void deleteService(UUID id);
}