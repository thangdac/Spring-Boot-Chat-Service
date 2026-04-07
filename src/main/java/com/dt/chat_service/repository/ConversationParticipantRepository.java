package com.dt.chat_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dt.chat_service.entity.ConversationParticipant;

@Repository
public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, UUID> {

    // Lấy tất cả participant của 1 conversation
    // Dùng khi gửi message — cần biết ai trong room
    List<ConversationParticipant> findByConversationId(UUID conversationId);

    // Kiểm tra user có trong conversation không
    // Dùng để phân quyền — không phải member thì không đọc được
    boolean existsByConversationIdAndUserId(UUID conversationId, UUID userId);

    // Tìm participant cụ thể — dùng khi kick member hoặc update lastReadAt
    Optional<ConversationParticipant> findByConversationIdAndUserId(UUID conversationId, UUID userId);
}
