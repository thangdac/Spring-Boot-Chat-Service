package com.dt.chat_service.mapper;

import org.mapstruct.Mapper;

import com.dt.chat_service.dto.request.PermissionRequest;
import com.dt.chat_service.dto.response.PermissionResponse;
import com.dt.chat_service.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
