package com.dt.chat_service.repository;

import com.dt.chat_service.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, String> {

    void deleteByExpiresAtBefore(Instant now);
}
