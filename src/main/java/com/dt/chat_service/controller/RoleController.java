package com.dt.chat_service.controller;

import com.dt.chat_service.dto.request.RoleRequest;
import com.dt.chat_service.dto.response.APIResponse;
import com.dt.chat_service.dto.response.RoleResponse;
import com.dt.chat_service.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @GetMapping
    public APIResponse<List<RoleResponse>> getAllRoles() {
        return APIResponse.<List<RoleResponse>>builder()
                .result(roleService.getAllRoles())
                .build();
    }

    @PostMapping
    public APIResponse<RoleResponse> createRole(@RequestBody @Valid RoleRequest request) {
        return APIResponse.<RoleResponse>builder()
                .result(roleService.createRole(request))
                .build();
    }

    @DeleteMapping("/{name}")
    public APIResponse<Void> deleteRole(@PathVariable  String name) {
        roleService.deleteRole(name);
        return APIResponse.<Void>builder()
                .build();
    }

}
