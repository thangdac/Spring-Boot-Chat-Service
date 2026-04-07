package com.dt.chat_service.dto.request;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadRequest {

    private UUID conversationId;
}
