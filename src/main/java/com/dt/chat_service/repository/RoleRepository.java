package com.dt.chat_service.repository;

import com.dt.chat_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {

    boolean existsByName(String name);

}
