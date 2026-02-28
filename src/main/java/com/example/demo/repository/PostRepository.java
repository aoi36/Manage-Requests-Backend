package com.example.demo.repository;

import com.example.demo.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Page<Post> findPageByTitleContainingIgnoreCase(String name, Pageable pageable);
    List<Post> findByFileNameContainingIgnoreCase(String name);
    boolean existsByServiceId(UUID serviceId);
}
