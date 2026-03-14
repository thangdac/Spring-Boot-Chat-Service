package com.dt.chat_service.controller;

import com.dt.chat_service.dto.request.PermissionRequest;
import com.dt.chat_service.dto.response.APIResponse;
import com.dt.chat_service.dto.response.PermissionResponse;
import com.dt.chat_service.service.PermissionService;
import jakarta.validation.Valid;
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
                .result(permissionService.getAllPermissions())
                .build();
    }

    @PostMapping
    public APIResponse<PermissionResponse> createPermission(@RequestBody @Valid PermissionRequest request) {
        return APIResponse.<PermissionResponse>builder()
                .result(permissionService.createPermission(request))
                .build();
    }

    @DeleteMapping("/{name}")
    public APIResponse<Void> deletePermission(@PathVariable  String name) {
        permissionService.deletePermission(name);
        return APIResponse.<Void>builder()
                .build();
    }

}
