package com.dt.chat_service.controller;


import com.dt.chat_service.dto.response.APIResponse;
import com.dt.chat_service.dto.response.UserStatusResponse;
import com.dt.chat_service.enums.UserStatus;
import com.dt.chat_service.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)

public class UserStatusController {

    final UserStatusService userStatusService;

    // GET /api/users/{id}/status
    @GetMapping("/{id}/status")
    public ResponseEntity<APIResponse<UserStatusResponse>> getStatus(@PathVariable String id) {

        return ResponseEntity.ok(
                APIResponse.<UserStatusResponse>builder()
                        .result(new UserStatusResponse(id, userStatusService.getStatus(id)))
                        .build()
        );

    }

    @GetMapping("/online")
    public ResponseEntity<APIResponse<List<UserStatusResponse>>> getOnlineUsers() {

        return ResponseEntity.ok(
                APIResponse.<List<UserStatusResponse>>builder()
                        .result(userStatusService.getOnlineUsers().stream()
                                .map(uid -> UserStatusResponse.builder()
                                        .userId(uid)
                                        .status(UserStatus.ONLINE)
                                        .build())
                                .collect(Collectors.toList()))
                        .build()
        );
    }

}
