package com.dt.chat_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "token_blacklist")
@Getter
@Setter
@NoArgsConstructor
public class TokenBlacklist {

    @Id
    private String jti; // JWT ID, lấy từ claim "jti" trong token

    @Column(nullable = false)
    private Instant expiresAt; // để cleanup định kỳ, tránh bảng phình to
}
