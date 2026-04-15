package com.dt.chat_service.service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.dt.chat_service.dto.request.SendMessageRequest;
import com.dt.chat_service.dto.response.MessageResponse;
import com.dt.chat_service.dto.response.UnReadResponse;
import com.dt.chat_service.entity.*;
import com.dt.chat_service.enums.MessageStatus;
import com.dt.chat_service.exception.AppException;
import com.dt.chat_service.exception.ErrorCode;
import com.dt.chat_service.mapper.MessageMapper;
import com.dt.chat_service.repository.*;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@Data
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MessageService {

    UserRepository userRepository;
    MessageRepository messageRepository;
    MessageReceiptRepository messageReceiptRepository;
    ConversationRepository conversationRepository;
    ConversationParticipantRepository conversationParticipantRepository;
    MessageMapper messageMapper;
    SimpMessagingTemplate messagingTemplate;

    // gửi tin nhắn
    public MessageResponse sendMessage(SendMessageRequest request, UUID senderId) {

        // kiểm tra sender có trong conversation không
        if (!conversationParticipantRepository.existsByConversationIdAndUserId(request.getConversationId(), senderId)) {
            throw new AppException(ErrorCode.NOT_A_MEMBER);
        }

        Conversation conversation = conversationRepository
                .findById(request.getConversationId())
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // tạo message
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(request.getContent());
        message.setType(request.getType());

        // tại receipt cho từ member trừ sender
        // lấy user trong conversation
        List<ConversationParticipant> participants =
                conversationParticipantRepository.findByConversationId(request.getConversationId());
        // lọc
        for (ConversationParticipant participant : participants) {
            if (!participant.getUser().getId().equals(senderId)) {
                MessageReceipt receipt = new MessageReceipt();
                receipt.setMessage(message);
                receipt.setUser(participant.getUser());
                receipt.setStatus(MessageStatus.DELIVERED);
                message.getReceipts().add(receipt);
            }
        }

        // nếu có ít nhất 1 người nhận
        if (!message.getReceipts().isEmpty()) {
            message.setOverallStatus(MessageStatus.DELIVERED);
        }

        messageRepository.save(message);

        MessageResponse response = messageMapper.toMessageResponse(message);

        // Broadcast real-time cho tất cả trong conversation
        messagingTemplate.convertAndSend("/topic/conversation." + request.getConversationId(), response);

        return response;
    }

    // user đánh dấu đã đọc trong conversation
    public void markAsRead(UUID conversationId, UUID userId) {

        List<MessageReceipt> unRead =
                messageReceiptRepository.findUnreadByConversation(conversationId, userId, MessageStatus.READ);

        if (unRead.isEmpty()) return;

        Instant now = Instant.now();

        for (MessageReceipt receipt : unRead) {
            receipt.setStatus(MessageStatus.READ);
            receipt.setReadAt(now);
        }
        messageReceiptRepository.saveAll(unRead);

        // kiểm tra từ message nếu tất cả để READ thì update overallStatus = READ
        Set<Message> affectedMessages =
                unRead.stream().map(MessageReceipt::getMessage).collect(Collectors.toSet());

        for (Message a : affectedMessages) {
            boolean allRead = a.getReceipts().stream().allMatch(m -> m.getStatus() == MessageStatus.READ);
            if (allRead) {
                a.setOverallStatus(MessageStatus.READ);
                messageRepository.save(a);
            }
        }
    }

    public List<MessageResponse> getHistory(UUID conversationId, UUID userId, Instant before, int limit) {

        // kiểm tra user trong conversation
        if (!conversationParticipantRepository.existsByConversationIdAndUserId(conversationId, userId)) {
            throw new AppException(ErrorCode.NOT_A_MEMBER);
        }

        // phân trang cho tin nhắn
        Pageable pageable = PageRequest.of(0, Math.min(limit, 50));

        return messageRepository.findHistory(conversationId, before, pageable).stream()
                .map(messageMapper::toMessageResponse)
                .toList();
    }

    // đếm tin chưa đọc
    public UnReadResponse countUnRead(UUID conversationId, UUID userId) {

        long count = messageRepository.countUnread(conversationId, userId, MessageStatus.READ);

        return new UnReadResponse(count);
    }


}
