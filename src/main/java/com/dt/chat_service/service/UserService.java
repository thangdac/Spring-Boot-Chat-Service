package com.dt.chat_service.service;

import com.dt.chat_service.dto.request.UserCreationRequest;
import com.dt.chat_service.dto.request.UserUpdateRequest;
import com.dt.chat_service.dto.response.UserResponse;
import com.dt.chat_service.entity.User;
import com.dt.chat_service.mapper.UserMapper;
import com.dt.chat_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return userMapper.toUserResponse(
                userRepository.findByUsername(name));
    }

    //get user by id
    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElse(null));
    }

    public UserResponse createUser(UserCreationRequest request) {

        User user = userMapper.toUser(request);
        return userMapper.toUserResponse(
                userRepository.save(user));
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        userMapper.updateUser(request, user);
        return userMapper.toUserResponse(
                userRepository.save(user));
     }

     public void deleteUser(String id) {
        if(!userRepository.existsById(id)) {
            throw new RuntimeException("User not found") ;}
        userRepository.deleteById(id);
     }



}
