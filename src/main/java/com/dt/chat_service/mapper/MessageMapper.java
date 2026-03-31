package com.dt.chat_service.mapper;


import com.dt.chat_service.dto.response.MessageResponse;
import com.dt.chat_service.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "conversationId", source = "conversation.id")
    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "senderName", source = "sender.username")
    @Mapping(target = "senderAvatar", source = "sender.avatarUrl")
    MessageResponse toMessageResponse(Message message);

}
