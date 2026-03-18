package com.dt.chat_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantResponse {
    private UUID userId;
    private String username;
    private String avatarUrl;

    @JsonProperty("isAdmin")
    private Boolean isAdmin;
    private Instant joinedAt;
}
