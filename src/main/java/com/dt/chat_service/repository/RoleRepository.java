package com.dt.chat_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dt.chat_service.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> {

    boolean existsByName(String name);
}
