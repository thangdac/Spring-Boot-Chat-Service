package com.dt.chat_service.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

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
public class SendMessageRequest {

    @NonNull
    UUID conversationId;

    @NotBlank
    String content;

    @Builder.Default
    MessageType type = MessageType.TEXT;
}
