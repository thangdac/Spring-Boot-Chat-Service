package com.dt.chat_service.service;

import com.dt.chat_service.dto.request.UserCreationRequest;
import com.dt.chat_service.dto.request.UserUpdateRequest;
import com.dt.chat_service.dto.response.UserResponse;
import com.dt.chat_service.entity.User;
import com.dt.chat_service.exception.AppException;
import com.dt.chat_service.exception.ErrorCode;
import com.dt.chat_service.mapper.UserMapper;
import com.dt.chat_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    //get user by username
    public UserResponse getUserByUsername(String name) {
        return userMapper.toUserResponse(userRepository.findByUsername(name)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    //get user by id
    public UserResponse getUserById(UUID id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse createUser(UserCreationRequest request) {

        if(userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        User user = userMapper.toUser(request);

        return userMapper.toUserResponse(
                userRepository.save(user));
    }

    public UserResponse updateUser(UUID id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(request, user);
        return userMapper.toUserResponse(
                userRepository.save(user));
     }

     public void deleteUser(UUID id) {
        if(!userRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND) ;}
        userRepository.deleteById(id);
     }



}
