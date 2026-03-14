package com.dt.chat_service.mapper;


import com.dt.chat_service.dto.request.RoleRequest;
import com.dt.chat_service.dto.response.RoleResponse;
import com.dt.chat_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

}
