package com.dt.chat_service.dto.response;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadReceiptEvent {

    private UUID conversationId;
    private UUID readByUserId;
    private Instant readAt;
}
