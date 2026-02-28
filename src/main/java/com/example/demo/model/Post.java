package com.example.demo.model;

import com.example.demo.constant.UrgencyLevel;
import com.example.demo.model.common.AbstractAuditingEntity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String fileName;
    private String filePath;
    private Long fileSize;
    private String title;
    private boolean isViewed;
    private String description;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "sub_service")
    private String subService;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency_level")
    private UrgencyLevel urgencyLevel;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_service_id")
    private BusinessService service;
}