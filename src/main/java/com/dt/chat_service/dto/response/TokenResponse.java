package com.dt.chat_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
    String accessToken;
    String refreshToken;
}
