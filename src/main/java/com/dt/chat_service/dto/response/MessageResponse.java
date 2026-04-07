package com.dt.chat_service.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.dt.chat_service.enums.MessageStatus;
import com.dt.chat_service.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {

    UUID id;
    UUID conversationId;
    UUID senderId;
    String senderName;
    String senderAvatar;
    String content;
    MessageType type;
    MessageStatus overallStatus;
    Instant createdAt;
}
