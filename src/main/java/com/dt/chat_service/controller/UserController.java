package com.dt.chat_service.controller;

import com.dt.chat_service.dto.request.UserCreationRequest;
import com.dt.chat_service.dto.request.UserUpdateRequest;
import com.dt.chat_service.dto.response.APIResponse;
import com.dt.chat_service.dto.response.UserResponse;
import com.dt.chat_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GetMapping
    public APIResponse<List<UserResponse>> getAllUsers() {
        return APIResponse.<List<UserResponse>>builder()
                .message("Users retrieved successfully")
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/search")
    public APIResponse<UserResponse> getUserByUsername(@RequestParam  String username) {
        return APIResponse.<UserResponse>builder()
                .message("User found")
                .result(userService.getUserByUsername(username))
                .build();
    }

    @GetMapping("/{id}")
    public APIResponse<UserResponse> getUserById(@PathVariable UUID id) {
        return APIResponse.<UserResponse>builder()
                .message("User found")
                .result(userService.getUserById(id))
                .build();
     }

    @PostMapping
    public APIResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return APIResponse.<UserResponse>builder()
                .message("User created successfully")
                .result(userService.createUser(request))
                .build();
     }

     @PutMapping("/{id}")
     public APIResponse<UserResponse> updateUser(@PathVariable UUID id, @RequestBody @Valid UserUpdateRequest request) {
         return APIResponse.<UserResponse>builder()
                 .message("User updated successfully")
                 .result(userService.updateUser(id, request))
                 .build();
      }

      @DeleteMapping("/{id}")
      public APIResponse<Void> deleteUser(@PathVariable UUID id) {
          userService.deleteUser(id);
          return APIResponse.<Void>builder()
                  .message("User deleted successfully")
                  .build();
       }

}
