package com.dt.chat_service.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dt.chat_service.entity.Message;
import com.dt.chat_service.enums.MessageStatus;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    // lấy lịch sử tin nhắn của 1 conversation
    @Query(
            """
			SELECT m FROM Message m
			WHERE m.conversation.id = :conversationId
				AND (:before IS NULL OR m.createdAt < :before)
			ORDER BY m.createdAt DESC
			""")
    List<Message> findHistory(
            @Param("conversationId") UUID conversationId, @Param("before") Instant before, Pageable pageable);

    @Query(
            """
			SELECT COUNT (m) FROM MessageReceipt m
			WHERE m.message.conversation.id = :conversationId
				AND m.user.id = :userId
				AND m.status <> :status
			""")
    long countUnread(
            @Param("conversationId") UUID conversationId,
            @Param("userId") UUID userId,
            @Param("status") MessageStatus status);
}
