package com.dt.chat_service.entity;

import com.dt.chat_service.enums.MessageStatus;
import com.dt.chat_service.enums.MessageType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "messages",
        indexes = @Index(
                name = "idx_messages_conversation_created",
                columnList = "conversation_id, created_at DESC"))
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type = MessageType.TEXT;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus overallStatus = MessageStatus.SENT;

    @OneToMany(mappedBy = "message",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<MessageReceipt> receipts = new ArrayList<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;
}

