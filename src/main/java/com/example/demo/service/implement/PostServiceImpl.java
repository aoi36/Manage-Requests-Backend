package com.example.demo.service.implement;

import com.example.demo.dto.request.PostRequest;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.PostResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.PostMapper;
import com.example.demo.model.BusinessService;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.BusinessServiceRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.interfaces.PostService;
import com.example.demo.utils.SortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BusinessServiceRepository businessServiceRepository; // Inject the new repository
    private final PostMapper postMapper;
    private final SortUtils sortUtils;
    private final String UPLOAD_DIR = "uploads/";

    @Override
    public PostResponse createPost(PostRequest postRequest, MultipartFile file) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BusinessService businessService = null;
        if (postRequest.getServiceId() != null) {
            businessService = businessServiceRepository.findById(postRequest.getServiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Business service not found"));
        }

        String storedFilePath = storeFile(file);

        Post post = Post.builder()
                .user(currentUser)
                .fileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .description(postRequest.getDescription())
                .filePath(storedFilePath)
                .isViewed(false)
                .title(postRequest.getTitle())
                .contactInfo(postRequest.getContactInfo())
                .email(postRequest.getEmail())
                .phoneNumber(postRequest.getPhoneNumber())
                .service(businessService)
                .subService(postRequest.getSubService())
                .urgencyLevel(postRequest.getUrgencyLevel())
                .build();

        postRepository.save(post);
        return postMapper.toPostResponse(post);
    }

    @Override
    public PageResponse<?> getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDirection){
        if (pageNumber < 1) {
            pageNumber = 1;
        }

        Sort sort = sortUtils.buildSort(sortBy, sortDirection);
        Pageable pageable = PageRequest.of(pageNumber - 1 , pageSize, sort);

        Page<Post> postsPage = postRepository.findAll(pageable);

        var postResponse = postsPage.getContent().stream().map(postMapper::toPostSummaryResponse).toList();

        return PageResponse.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(postsPage.getTotalElements())
                .totalPages(postsPage.getTotalPages())
                .content(postResponse)
                .build();
    }

    @Override
    public PostResponse getPostDetail(UUID postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin && !post.isViewed()) {
            post.setViewed(true);
            postRepository.save(post);
        }

        return postMapper.toPostResponse(post);
    }

    @Override
    public PageResponse<?> searchPostsByName(
            String name,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection
    ) {
        if (pageNumber < 1) {
            pageNumber = 1;
        }

        Sort sort = sortUtils.buildSort(sortBy, sortDirection);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<Post> postsPage =
                postRepository.findPageByTitleContainingIgnoreCase(name, pageable);

        var postResponses = postsPage.getContent()
                .stream()
                .map(postMapper::toPostSummaryResponse)
                .toList();

        return PageResponse.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(postsPage.getTotalElements())
                .totalPages(postsPage.getTotalPages())
                .content(postResponses)
                .build();
    }

    private String storeFile(MultipartFile file) {
        try {
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filePath.toString();
        } catch (Exception e) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Resource downloadFile(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        try {

            Path filePath = Paths.get(post.getFilePath()).normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or not readable: " + post.getFileName());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public String getPostFileName(UUID postId) {
        return postRepository.findById(postId)
                .map(Post::getFileName)
                .orElse("downloaded_file");
    }
}

