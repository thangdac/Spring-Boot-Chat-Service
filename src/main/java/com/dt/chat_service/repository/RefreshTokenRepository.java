package com.dt.chat_service.repository;

import com.dt.chat_service.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findAllByUserId(UUID userId);

    void deleteByTokenHash(String tokenHash);

    // Cleanup token hết hạn — gọi bằng @Scheduled
    void deleteByExpiresAtBefore(Instant now);
}
