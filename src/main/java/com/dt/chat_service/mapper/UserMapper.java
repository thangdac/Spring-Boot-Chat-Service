package com.dt.chat_service.mapper;

import com.dt.chat_service.dto.request.UserCreationRequest;
import com.dt.chat_service.dto.request.UserUpdateRequest;
import com.dt.chat_service.dto.response.UserResponse;
import com.dt.chat_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")

public interface UserMapper {

    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    void updateUser(UserUpdateRequest request, @MappingTarget User user);
}
