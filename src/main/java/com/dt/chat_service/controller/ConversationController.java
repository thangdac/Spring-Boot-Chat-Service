package com.dt.chat_service.controller;


import com.dt.chat_service.dto.request.CreateConversationRequest;
import com.dt.chat_service.dto.response.APIResponse;
import com.dt.chat_service.dto.response.ConversationResponse;
import com.dt.chat_service.service.ConversationService;
import com.dt.chat_service.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    // Tạo conversation mới (DIRECT hoặc GROUP)
    @PostMapping
    public ResponseEntity<APIResponse<ConversationResponse>> create(
            @RequestBody @Valid CreateConversationRequest request) {

        UUID userId = SecurityUtils.getCurrentUserId(); // ← gọi trực tiếp
        ConversationResponse response = conversationService.createConversation(request, userId);

        return ResponseEntity.ok(APIResponse.<ConversationResponse>builder()
                .result(response)
                .build());
    }

    // Lấy danh sách conversation của mình
    @GetMapping
    public ResponseEntity<APIResponse<List<ConversationResponse>>> getMyConversations() {

        UUID userId = SecurityUtils.getCurrentUserId();
        List<ConversationResponse> responses = conversationService.getMyConversations(userId);

        return ResponseEntity.ok(APIResponse.<List<ConversationResponse>>builder()
                .result(responses)
                .build());
    }

    // Lấy chi tiết 1 conversation
    @GetMapping("/{conversationId}")
    public ResponseEntity<APIResponse<ConversationResponse>> getConversation(
            @PathVariable UUID conversationId) {

        UUID userId = SecurityUtils.getCurrentUserId();
        ConversationResponse response = conversationService
                .getConversation(conversationId, userId);

        return ResponseEntity.ok(APIResponse.<ConversationResponse>builder()
                .result(response)
                .build());
    }


}
