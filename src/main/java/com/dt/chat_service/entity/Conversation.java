package com.dt.chat_service.entity;

import com.dt.chat_service.enums.ConversationType;
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

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConversationType type;

    private String name;

    private String avatarUrl;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ConversationParticipant> participants = new ArrayList<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    // Trong Conversation.java — thêm field này
    @OneToMany(mappedBy = "conversation",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @OrderBy("createdAt DESC") // sắp xếp mới nhất lên đầu
    private List<Message> messages = new ArrayList<>();


}
