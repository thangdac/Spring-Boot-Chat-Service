package com.dt.chat_service.controller;

import com.dt.chat_service.dto.request.SendMessageRequest;
import com.dt.chat_service.dto.response.APIResponse;
import com.dt.chat_service.dto.response.MessageResponse;
import com.dt.chat_service.dto.response.UnReadResponse;
import com.dt.chat_service.service.MessageService;
import com.dt.chat_service.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    //send message
    @PostMapping
    public ResponseEntity<APIResponse<MessageResponse>> sendMessage(@RequestBody @Valid SendMessageRequest request){

        UUID senderId = SecurityUtils.getCurrentUserId();
        MessageResponse messageResponse = messageService.sendMessage(request, senderId);

        return ResponseEntity.ok(APIResponse
                                    .<MessageResponse>builder()
                                    .message("send message susses")
                                    .result(messageResponse)
                                    .build());

    }

    //history message
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<APIResponse<List<MessageResponse>>> historyMessage(
                                                                             @PathVariable UUID conversationId,
                                                                             @RequestParam(required = false) Instant before,
                                                                             @RequestParam(defaultValue = "20") int limit){

        UUID userId = SecurityUtils.getCurrentUserId();
        List<MessageResponse> messageResponses = messageService.getHistory(conversationId, userId,before,limit);

        return ResponseEntity.ok(APIResponse.<List<MessageResponse>>builder()
                        .message("send history message susses")
                        .result(messageResponses)
                        .build());

    }

    //count unRead message
    @GetMapping("/conversation/{conversationId}/unRead")
    public ResponseEntity<APIResponse<UnReadResponse>> countUnRead(@PathVariable UUID conversationId){

        UUID userId = SecurityUtils.getCurrentUserId();

        UnReadResponse unRead = messageService.countUnRead(conversationId,userId);

        return ResponseEntity.ok(APIResponse.<UnReadResponse>builder()
                        .message("send unRead susses")
                        .result(unRead)
                        .build()
        );

    }

    @PutMapping("/conversation/{conversationId}/read")
    public ResponseEntity<APIResponse<Void>> markAsRead(
            @PathVariable UUID conversationId) {

        UUID userId = SecurityUtils.getCurrentUserId();
        messageService.markAsRead(conversationId, userId);

        return ResponseEntity.ok(APIResponse.<Void>builder()
                .message("Marked as read")
                .build());
    }

}
