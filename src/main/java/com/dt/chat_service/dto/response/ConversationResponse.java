package com.dt.chat_service.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.dt.chat_service.enums.ConversationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {

    private UUID id;
    private ConversationType type;
    private String name;
    private String avatarUrl;
    private List<ParticipantResponse> participants;
    private Instant createdAt;
    private Instant updatedAt;
}
