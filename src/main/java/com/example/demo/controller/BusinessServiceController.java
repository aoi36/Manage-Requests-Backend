package com.example.demo.controller;

import com.example.demo.dto.request.BusinessServiceRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.service.interfaces.BusinessServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/services")
public class BusinessServiceController {

    private final BusinessServiceService businessServiceService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createService(@RequestBody BusinessServiceRequest request) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Service created successfully", businessServiceService.createService(request))
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllServices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "displayOrder") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Services fetched successfully",
                        businessServiceService.getAllServices(page, size, sortBy, sortDirection))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Service details fetched successfully", businessServiceService.getServiceById(id))
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateService(@PathVariable UUID id, @RequestBody BusinessServiceRequest request) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Service updated successfully", businessServiceService.updateService(id, request))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteService(@PathVariable UUID id) {
        businessServiceService.deleteService(id);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Service deleted successfully", null)
        );
    }
}