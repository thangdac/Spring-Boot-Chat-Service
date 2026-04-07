package com.dt.chat_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dt.chat_service.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String> {
    boolean existsByName(String name);
}
