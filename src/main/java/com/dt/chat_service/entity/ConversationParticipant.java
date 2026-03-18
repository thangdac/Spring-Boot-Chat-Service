package com.dt.chat_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "conversation_participants",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"conversation_id", "user_id"}))
@Data
@NoArgsConstructor
public class ConversationParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean isAdmin = false; // true nếu là người tạo nhóm

    private Instant lastReadAt; // dùng để tính unread count

    @Column(nullable = false, updatable = false)
    private Instant joinedAt = Instant.now();

}
