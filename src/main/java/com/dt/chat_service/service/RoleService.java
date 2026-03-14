package com.dt.chat_service.service;

import com.dt.chat_service.dto.request.RoleRequest;
import com.dt.chat_service.dto.response.RoleResponse;
import com.dt.chat_service.entity.Permission;
import com.dt.chat_service.entity.Role;
import com.dt.chat_service.mapper.RoleMapper;
import com.dt.chat_service.repository.PermissionRepository;
import com.dt.chat_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {

    RoleRepository roleRepository;
    RoleMapper  roleMapper;
    PermissionRepository permissionRepository;

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public RoleResponse createRole(RoleRequest request) {
        Role role = roleMapper.toRole(request);

        // ✅ Fetch permissions từ DB rồi gán vào role
        Set<Permission> permissions = new HashSet<>(
                permissionRepository.findAllById(request.getPermissions())
        );
        role.setPermissions(permissions);

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public void deleteRole(String id) {
        if(!roleRepository.existsById(id)) {
            throw new RuntimeException("User not found") ;
        }
        roleRepository.deleteById(id);
    }

}
