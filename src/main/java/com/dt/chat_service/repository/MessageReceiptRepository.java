package com.dt.chat_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dt.chat_service.entity.MessageReceipt;
import com.dt.chat_service.enums.MessageStatus;

public interface MessageReceiptRepository extends JpaRepository<MessageReceipt, UUID> {

    Optional<MessageReceipt> findByMessageIdAndUserId(UUID messageId, UUID userId);

    // lấy tất cả receipt chưa đọc trong conversation
    @Query(
            """
		SELECT m FROM MessageReceipt m
		WHERE m.message.conversation.id = :conversationId
		AND m.user.id = :userId
		AND m.status <> :status
		""")
    List<MessageReceipt> findUnreadByConversation(
            @Param("conversationId") UUID conversationId,
            @Param("userId") UUID userId,
            @Param("status") MessageStatus status);
}
