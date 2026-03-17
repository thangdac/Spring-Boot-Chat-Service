package com.dt.chat_service.controller;

import com.dt.chat_service.dto.request.LoginRequest;
import com.dt.chat_service.dto.request.RefreshRequest;
import com.dt.chat_service.dto.response.APIResponse;
import com.dt.chat_service.dto.response.IntrospectResponse;
import com.dt.chat_service.dto.response.TokenResponse;
import com.dt.chat_service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<APIResponse<TokenResponse>> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletRequest httpRequest) {
        String deviceInfo = httpRequest.getHeader("User-Agent");
        TokenResponse tokens = authService.login(request, deviceInfo);
        return ResponseEntity.ok(APIResponse.<TokenResponse>builder()
                .result(tokens)
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<APIResponse<TokenResponse>> refresh(
            @RequestBody @Valid RefreshRequest request) {
        TokenResponse tokens = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(APIResponse.<TokenResponse>builder()
                .result(tokens)
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<APIResponse<Void>> logout(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid RefreshRequest request) {
        String accessToken = authHeader.substring(7);
        authService.logout(accessToken, request.getRefreshToken());
        return ResponseEntity.ok(APIResponse.<Void>builder()
                .message("Logout successful")
                .build());
    }

    @PostMapping("/introspect")
    public ResponseEntity<APIResponse<IntrospectResponse>> introspect(
            @RequestHeader("Authorization") String authHeader) {
        String accessToken = authHeader.substring(7);
        IntrospectResponse result = authService.introspect(accessToken);
        return ResponseEntity.ok(APIResponse.<IntrospectResponse>builder()
                .result(result)
                .build());
    }

}
