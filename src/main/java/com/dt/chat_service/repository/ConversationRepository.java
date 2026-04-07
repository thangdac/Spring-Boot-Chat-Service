package com.dt.chat_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dt.chat_service.entity.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    // Tìm tất cả conversation của 1 user
    // JOIN qua bảng participants để lọc theo userId
    @Query(
            """
		SELECT c FROM Conversation c
		JOIN c.participants p
		WHERE p.user.id = :userId
		ORDER BY c.updatedAt DESC
	""")
    List<Conversation> findAllByUserId(@Param("userId") UUID userId);

    // Tìm conversation DIRECT giữa 2 user cụ thể
    // Dùng khi tạo chat 1-1 — kiểm tra đã tồn tại chưa
    @Query(
            """
		SELECT c FROM Conversation c
		JOIN c.participants p1
		JOIN c.participants p2
		WHERE c.type = 'DIRECT'
		AND p1.user.id = :userId1
		AND p2.user.id = :userId2
	""")
    Optional<Conversation> findDirectConversation(@Param("userId1") UUID userId1, @Param("userId2") UUID userId2);
}
