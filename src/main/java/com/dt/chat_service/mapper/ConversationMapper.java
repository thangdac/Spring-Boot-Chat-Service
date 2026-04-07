package com.dt.chat_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dt.chat_service.dto.response.ConversationResponse;
import com.dt.chat_service.dto.response.ParticipantResponse;
import com.dt.chat_service.entity.Conversation;
import com.dt.chat_service.entity.ConversationParticipant;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

    // Conversation → ConversationResponse
    // participants tự map qua method bên dưới
    @Mapping(target = "participants", source = "participants")
    ConversationResponse toConversationResponse(Conversation conversation);

    // ConversationParticipant → ParticipantResponse
    // userId lấy từ user.id, username lấy từ user.username
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "avatarUrl", source = "user.avatarUrl")
    @Mapping(target = "isAdmin", source = "admin")
    ParticipantResponse toParticipantResponse(ConversationParticipant participant);
}
