package com.dt.chat_service.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;

import com.dt.chat_service.enums.MessageStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "message_receipts",
        uniqueConstraints =
                @UniqueConstraint(columnNames = {"message_id", "user_id"})) // mỗi user chỉ có 1 receipt/message
@Getter
@Setter
@NoArgsConstructor
public class MessageReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Receipt này thuộc message nào
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    // Receipt này của user nào
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus status = MessageStatus.DELIVERED;

    private Instant deliveredAt; // lúc nhận được
    private Instant readAt; // lúc đọc
}
