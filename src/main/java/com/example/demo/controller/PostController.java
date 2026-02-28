package com.example.demo.controller;

import com.example.demo.dto.request.PostRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.PostResponse;
import com.example.demo.dto.response.PostSummaryResponse;
import com.example.demo.service.interfaces.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostDetail(@PathVariable UUID id) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Post retrieved successfully", postService.getPostDetail(id)));
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> createPost(
            @Valid @RequestPart("post") PostRequest postRequest,
            @RequestPart("file") MultipartFile file
    ) {
        PostResponse response = postService.createPost(postRequest, file);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Post created successfully", response)
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "16") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        PageResponse<?> response = postService.getAllPosts(page, size, sortBy, sortDirection);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Posts fetched successfully", response)
        );
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> searchPosts(
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "16") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        PageResponse<?> response =
                postService.searchPostsByName(name, page, size, sortBy, sortDirection);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Posts found successfully", response)
        );
    }

    @GetMapping("/{id}/download")

    public ResponseEntity<?> downloadFile(@PathVariable UUID id, HttpServletRequest request) {

        Resource resource = postService.downloadFile(id);

        String fileName = postService.getPostFileName(id);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
