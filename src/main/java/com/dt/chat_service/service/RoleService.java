package com.dt.chat_service.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.dt.chat_service.dto.request.RoleRequest;
import com.dt.chat_service.dto.response.RoleResponse;
import com.dt.chat_service.entity.Permission;
import com.dt.chat_service.entity.Role;
import com.dt.chat_service.exception.AppException;
import com.dt.chat_service.exception.ErrorCode;
import com.dt.chat_service.mapper.RoleMapper;
import com.dt.chat_service.repository.PermissionRepository;
import com.dt.chat_service.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {

    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public RoleResponse createRole(RoleRequest request) {

        if (!roleRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        Role role = roleMapper.toRole(request);

        // ✅ Fetch permissions từ DB rồi gán vào role
        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.getPermissions()));
        role.setPermissions(permissions);

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public void deleteRole(String id) {
        if (!roleRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        roleRepository.deleteById(id);
    }
}
