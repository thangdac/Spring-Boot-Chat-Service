package com.dt.chat_service.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dt.chat_service.entity.RefreshToken;
import com.dt.chat_service.entity.User;
import com.dt.chat_service.exception.AppException;
import com.dt.chat_service.exception.ErrorCode;
import com.dt.chat_service.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwt.refresh-token-expiry}")
    private Long refreshTokenExpiry;

    // Tạo refresh token mới và lưu vào DB
    public String createRefreshToken(User user, String deviceInfo) {
        String plainToken = UUID.randomUUID().toString();
        String tokenHash = hashToken(plainToken);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(tokenHash);
        refreshToken.setDeviceInfo(deviceInfo);
        refreshToken.setExpiresAt(Instant.now().plusMillis(refreshTokenExpiry));

        refreshTokenRepository.save(refreshToken);

        return plainToken; // trả về plain text cho client
    }

    // Validate refresh token khi client gọi /refresh
    public RefreshToken validate(String plainToken) {
        String tokenHash = hashToken(plainToken);

        RefreshToken refreshToken = refreshTokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        return refreshToken;
    }

    // Xóa refresh token khi logout
    public void revoke(String plainToken) {
        String tokenHash = hashToken(plainToken);
        refreshTokenRepository.deleteByTokenHash(tokenHash);
    }

    // Xóa tất cả refresh token của user — dùng khi đổi mật khẩu
    public void revokeAll(UUID userId) {
        refreshTokenRepository.deleteAll(refreshTokenRepository.findAllByUserId(userId));
    }

    // Hash SHA-256 trước khi lưu DB
    private String hashToken(String plainToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(plainToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new AppException(ErrorCode.TOKEN_HASH_ERROR);
        }
    }
}
