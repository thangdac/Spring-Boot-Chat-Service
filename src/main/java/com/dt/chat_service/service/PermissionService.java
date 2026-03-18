package com.dt.chat_service.service;

import com.dt.chat_service.dto.request.PermissionRequest;
import com.dt.chat_service.dto.response.PermissionResponse;
import com.dt.chat_service.entity.Permission;
import com.dt.chat_service.exception.AppException;
import com.dt.chat_service.exception.ErrorCode;
import com.dt.chat_service.mapper.PermissionMapper;
import com.dt.chat_service.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll()
                .stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    public PermissionResponse createPermission(PermissionRequest request) {

        if(!permissionRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        Permission permission = permissionMapper.toPermission(request);

        return permissionMapper.toPermissionResponse(
                permissionRepository.save(permission));
    }

    public void deletePermission(String id) {
        if(!permissionRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND) ;
        }
        permissionRepository.deleteById(id);
    }




}
