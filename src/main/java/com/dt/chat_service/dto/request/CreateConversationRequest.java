package com.dt.chat_service.dto.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.dt.chat_service.enums.ConversationType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateConversationRequest {

    // DIRECT hoặc GROUP
    @NotNull
    private ConversationType type;

    // Danh sách userId được mời vào
    // DIRECT: chỉ cần 1 userId
    // GROUP: có thể nhiều userId
    @NotEmpty
    private List<UUID> participantIds;

    // Chỉ cần điền nếu type = GROUP
    private String name;

    private String avatarUrl;
}
