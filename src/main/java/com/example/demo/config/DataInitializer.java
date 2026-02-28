package com.example.demo.config; // Adjust package as needed

import com.example.demo.constant.RoleName;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void initData() {
        initRoles();
        initAdminAccount();
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
        } else {
            log.info("Admin user already exists. Skipping.");
        }
    }
}