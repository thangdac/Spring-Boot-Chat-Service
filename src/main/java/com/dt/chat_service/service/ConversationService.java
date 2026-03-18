package com.dt.chat_service.service;

import com.dt.chat_service.dto.request.CreateConversationRequest;
import com.dt.chat_service.dto.response.ConversationResponse;
import com.dt.chat_service.entity.Conversation;
import com.dt.chat_service.entity.ConversationParticipant;
import com.dt.chat_service.entity.User;
import com.dt.chat_service.enums.ConversationType;
import com.dt.chat_service.exception.AppException;
import com.dt.chat_service.exception.ErrorCode;
import com.dt.chat_service.mapper.ConversationMapper;
import com.dt.chat_service.repository.ConversationParticipantRepository;
import com.dt.chat_service.repository.ConversationRepository;
import com.dt.chat_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {

    ConversationRepository conversationRepository;
    ConversationParticipantRepository participantRepository;
    UserRepository userRepository;
    ConversationMapper conversationMapper;

    public ConversationResponse createConversation(
            CreateConversationRequest request, UUID creatorId) {

        // Validate theo type
        if (request.getType() == ConversationType.DIRECT) {
            if (request.getParticipantIds().size() != 1) {
                throw new AppException(ErrorCode.INVALID_INPUT);
            }
            // Trả về conversation cũ nếu đã tồn tại
            UUID targetId = request.getParticipantIds().get(0);
            Optional<Conversation> existing = conversationRepository
                    .findDirectConversation(creatorId, targetId);
            if (existing.isPresent()) {
                return conversationMapper.toConversationResponse(existing.get());
            }
        }

        if (request.getType() == ConversationType.GROUP
                && (request.getName() == null || request.getName().isBlank())) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }

        Conversation conversation = new Conversation();
        conversation.setType(request.getType());
        conversation.setName(request.getName());
        conversation.setAvatarUrl(request.getAvatarUrl());

        // Build participants và add vào list — cascade tự save
        conversation.getParticipants().add(
                buildParticipant(conversation, creatorId, true));

        for (UUID participantId : request.getParticipantIds()) {
            if (!participantId.equals(creatorId)) {
                conversation.getParticipants().add(
                        buildParticipant(conversation, participantId, false));
            }
        }

        conversationRepository.save(conversation);

        return conversationMapper.toConversationResponse(conversation);
    }

    public List<ConversationResponse> getMyConversations(UUID userId) {
        return conversationRepository.findAllByUserId(userId)
                .stream()
                .map(conversationMapper::toConversationResponse)
                .toList();
    }

    public ConversationResponse getConversation(UUID conversationId, UUID userId) {
        if (!participantRepository.existsByConversationIdAndUserId(
                conversationId, userId)) {
            throw new AppException(ErrorCode.NOT_A_MEMBER);
        }

        return conversationRepository.findById(conversationId)
                .map(conversationMapper::toConversationResponse)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
    }

    private ConversationParticipant buildParticipant(
            Conversation conversation, UUID userId, boolean isAdmin) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        ConversationParticipant participant = new ConversationParticipant();
        participant.setConversation(conversation);
        participant.setUser(user);
        participant.setAdmin(isAdmin);
        return participant;
    }
}
