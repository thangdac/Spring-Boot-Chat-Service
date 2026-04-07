package com.dt.chat_service.controller;

import java.security.Principal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.dt.chat_service.dto.request.ReadRequest;
import com.dt.chat_service.dto.request.SendMessageRequest;
import com.dt.chat_service.dto.request.TypingRequest;
import com.dt.chat_service.dto.response.ReadReceiptEvent;
import com.dt.chat_service.dto.response.TypingEvent;
import com.dt.chat_service.service.MessageService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {

    MessageService messageService;
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload SendMessageRequest request, Principal principal) {
        UUID senderId = UUID.fromString(principal.getName());
        messageService.sendMessage(request, senderId);
    }

    @MessageMapping("/chat.read")
    public void readMessage(@Payload ReadRequest request, Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        messageService.markAsRead(request.getConversationId(), userId);

        // Notify cho tất cả trong conversation biết user này đã đọc
        ReadReceiptEvent event = new ReadReceiptEvent(request.getConversationId(), userId, Instant.now());
        messagingTemplate.convertAndSend("/topic/conversation." + request.getConversationId() + ".read", event);
    }

    // Client gửi typing indicator: /app/chat.typing
    @MessageMapping("/chat.typing")
    public void typing(@Payload TypingRequest request, Principal principal) {

        UUID userId = UUID.fromString(principal.getName());

        TypingEvent event = new TypingEvent(request.getConversationId(), userId, request.isTyping());

        // Broadcast cho tất cả trong conversation
        // Không lưu DB — chỉ là event real-time
        messagingTemplate.convertAndSend("/topic/conversation." + request.getConversationId() + ".typing", event);
    }
}
