package com.dt.chat_service.dto.request;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TypingRequest {

    private UUID conversationId;
    private boolean Typing; // true = đang nhập, false = ngừng nhập
}
