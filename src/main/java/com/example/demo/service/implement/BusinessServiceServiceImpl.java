package com.example.demo.service.implement;

import com.example.demo.dto.request.BusinessServiceRequest;
import com.example.demo.dto.response.BusinessServiceResponse;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.BusinessServiceMapper;
import com.example.demo.model.BusinessService;
import com.example.demo.repository.BusinessServiceRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.interfaces.BusinessServiceService;
import com.example.demo.utils.SortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusinessServiceServiceImpl implements BusinessServiceService {

    private final BusinessServiceRepository businessServiceRepository;
    private final BusinessServiceMapper businessServiceMapper;
    private final SortUtils sortUtils;
    private final PostRepository postRepository;

    @Transactional
    @Override
    public BusinessServiceResponse createService(BusinessServiceRequest request) {
        BusinessService businessService = businessServiceMapper.toEntity(request);
        BusinessService savedService = businessServiceRepository.save(businessService);
        return businessServiceMapper.toResponse(savedService);
    }

    @Override
    public PageResponse<?> getAllServices(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        if (pageNumber < 1) pageNumber = 1;

        Sort sort = sortUtils.buildSort(sortBy, sortDirection);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<BusinessService> servicesPage = businessServiceRepository.findByIsActiveTrue(pageable);

        var content = servicesPage.getContent().stream()
                .map(businessServiceMapper::toResponse)
                .toList();

        return PageResponse.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(servicesPage.getTotalElements())
                .totalPages(servicesPage.getTotalPages())
                .content(content)
                .build();
    }

    @Override
    public BusinessServiceResponse getServiceById(UUID id) {
        BusinessService businessService = businessServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business service not found"));
        return businessServiceMapper.toResponse(businessService);
    }

    @Transactional
    @Override
    public BusinessServiceResponse updateService(UUID id, BusinessServiceRequest request) {
        BusinessService businessService = businessServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business service not found"));

        businessService.setName(request.getName());
        businessService.setDisplayOrder(request.getDisplayOrder());

        BusinessService updatedService = businessServiceRepository.save(businessService);
        return businessServiceMapper.toResponse(updatedService);
    }

    @Override
    public void deleteService(UUID id) {
        // 1. Find the service
        BusinessService businessService = businessServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ"));

        businessService.setActive(false);

        businessServiceRepository.save(businessService);

    }
}