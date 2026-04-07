package com.dt.chat_service.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class LoginRequest {

    @NotBlank(message = "USERNAME_BLANK")
    String username;

    @NotBlank(message = "PASSWORD_BLANK")
    String password;
}
