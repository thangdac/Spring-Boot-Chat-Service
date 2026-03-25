package com.dt.chat_service.dto.request;

import com.dt.chat_service.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

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

    MessageType type = MessageType.TEXT;
}
