package com.dt.chat_service.config;

import com.dt.chat_service.entity.Role;
import com.dt.chat_service.entity.User;
import com.dt.chat_service.enums.UserStatus;
import com.dt.chat_service.repository.RoleRepository;
import com.dt.chat_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataSeeder implements ApplicationRunner {

    final RoleRepository roleRepository;
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    String adminUsername;

    @Value("${app.admin.email}")
    String adminEmail;

    @Value("${app.admin.password}")
    String adminPassword;

    @Override
    public void run(ApplicationArguments args) {
        seedRoles();
        seedAdmin();
    }

    private void seedRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(List.of(
                    new Role("USER", "Default user", null),
                    new Role("ADMIN", "Administrator", null),
                    new Role("MODERATOR", "Moderator", null)
            ));
        }
    }

    private void seedAdmin() {
        if (userRepository.existsByUsername(adminUsername)) return;

        Role adminRole = roleRepository.findById("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

        // Dùng setter thay vì builder
        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setStatus(UserStatus.ACTIVE);
        admin.setRoles(Set.of(adminRole));

        userRepository.save(admin);
        log.info(">>> Admin account created: {}", adminUsername);
    }
}