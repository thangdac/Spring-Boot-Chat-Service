package com.dt.chat_service.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TypingEvent {

    private UUID conversationId;
    private UUID userId;
    private boolean typing;
}
