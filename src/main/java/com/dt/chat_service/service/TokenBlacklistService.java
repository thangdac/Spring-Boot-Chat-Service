package com.dt.chat_service.service;

import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.dt.chat_service.entity.TokenBlacklist;
import com.dt.chat_service.repository.TokenBlacklistRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TokenBlacklistService {

    TokenBlacklistRepository tokenBlacklistRepository;

    public void blacklistToken(String jti, Instant expiresAt) {

        TokenBlacklist entry = new TokenBlacklist();
        entry.setJti(jti);
        entry.setExpiresAt(expiresAt);
        tokenBlacklistRepository.save(entry);
    }

    public boolean isBlacklisted(String jti) {
        return tokenBlacklistRepository.existsById(jti);
    }

    // Tự động dọn dẹp mỗi đêm 3h sáng
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupExpired() {
        tokenBlacklistRepository.deleteByExpiresAtBefore(Instant.now());
    }
}
