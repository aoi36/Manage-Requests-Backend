package com.example.demo.config;

import com.example.demo.constant.RoleName;
import com.example.demo.model.BusinessService;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.BusinessServiceRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BusinessServiceRepository businessServiceRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void initData() {
        initRoles();
        initAdminAccount();
        initBusinessServices();
    }

    private void initRoles() {
        for (RoleName roleName : RoleName.values()) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                roleRepository.save(Role.builder().name(roleName).build());
                log.info("Role created: {}", roleName);
            }
        }
    }

    private void initAdminAccount() {
        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseThrow(() -> new IllegalStateException("Admin Role was not initialized"));

        if (userRepository.findByUsername("adminadmin").isEmpty()) {
            User adminUser = User.builder()
                    .username("adminadmin")
                    .password(passwordEncoder.encode("adminadmin"))
                    .role(adminRole)
                    .build();

            userRepository.save(adminUser);
            log.info("Admin user created: adminadmin / adminadmin");
        }
    }

    private void initBusinessServices() {

        List<String> services = List.of(
                "IPCAS",
                "AGRITAX (Thu ngân sách)",
                "BillPayment (Thu hóa đơn)",
                "Song phương kho bạc",
                "Song phương bảo hiểm xã hội",
                "E-Banking",
                "PaymentHub (Thanh toán tập trung)",
                "Hệ thống OSB",
                "Hệ thống quản lý mã định danh (Nickname/Alias)",
                "Hệ thống Văn bản định chế",
                "Hệ thống quản lý BackEnd-Admin",
                "Hệ thống eKYC-Backend",
                "Hệ thống mẫu biểu thông minh (SmartForm)",
                "Hệ thống Cảnh báo sớm Rủi ro tín dụng",
                "Hệ thống Hỗ trợ phê duyệt tín dụng vượt thẩm quyền",
                "FIMI",
                "CSP",
                "Hệ thống Email AD (Email Agribank)",
                "Hệ thống ARS",
                "Kinh phí công đoàn",
                "Quản lý tài sản trong kho tiền (AMS)",
                "Hệ thống Hóa đơn điện tử",
                "Hệ thống BeAdmin",
                "Hệ thống Vay online (Lending)",
                "Hệ thống PKI",
                "Hệ thống Quản lý nội bộ",
                "Hệ thống Phòng chống rửa tiền"
        );

        int order = 1;

        for (String serviceName : services) {
            if (businessServiceRepository.findByName(serviceName).isEmpty()) {
                BusinessService service = BusinessService.builder()
                        .name(serviceName)
                        .displayOrder(order++)
                        .isActive(true)
                        .build();

                businessServiceRepository.save(service);
                log.info("BusinessService created: {}", serviceName);
            }
        }
    }
}
