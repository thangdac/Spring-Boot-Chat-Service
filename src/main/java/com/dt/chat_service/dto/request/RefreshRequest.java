package com.dt.chat_service.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class RefreshRequest {
    @NotBlank
    String refreshToken;
}
