package com.example.demo.service.interfaces;

import com.example.demo.dto.request.PostRequest;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.PostResponse;
import com.example.demo.dto.response.PostSummaryResponse;
import com.example.demo.model.Post;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PostService {
    PostResponse createPost(PostRequest postRequest, MultipartFile file);
    PageResponse getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDirection);
    PostResponse getPostDetail(UUID postId);
    PageResponse searchPostsByName(String name, int pageNumber, int pageSize, String sortBy, String sortDirection);
    Resource downloadFile(UUID postId);
    String getPostFileName(UUID postId);
}
