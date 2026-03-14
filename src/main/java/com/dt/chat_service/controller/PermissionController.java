package com.dt.chat_service.controller;

import com.dt.chat_service.dto.request.PermissionRequest;
import com.dt.chat_service.dto.response.APIResponse;
import com.dt.chat_service.dto.response.PermissionResponse;
import com.dt.chat_service.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    PermissionService permissionService;

    @GetMapping
    public APIResponse<List<PermissionResponse>> getAllPermissions() {
        return APIResponse.<List<PermissionResponse>>builder()
                .code(200)
                .message("Permissions retrieved successfully")
                .result(permissionService.getAllPermissions())
                .build();
    }

    @PostMapping
    public APIResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        return APIResponse.<PermissionResponse>builder()
                .code(201)
                .message("Permission created successfully")
                .result(permissionService.createPermission(request))
                .build();
    }

    @DeleteMapping
    public APIResponse<Void> deletePermission(@RequestParam String name) {
        permissionService.deletePermission(name);
        return APIResponse.<Void>builder()
                .code(203)
                .message("Permission deleted successfully")
                .build();
    }

}
