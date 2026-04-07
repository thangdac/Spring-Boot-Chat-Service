package com.dt.chat_service.Security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dt.chat_service.entity.Role;
import com.dt.chat_service.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.access-token-expiry}")
    private long accessTokenExpiry;

    private SecretKey getSigningKey() {
        // đổi secret string thành SecretKey
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Tạo access token
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("roles", user.getRoles().stream().map(Role::getName).toList())
                .id(UUID.randomUUID().toString()) // jti — dùng cho blacklist
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiry))
                .signWith(getSigningKey())
                .compact();
    }

    // Parse và verify token — throw exception nếu invalid
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Kiểm tra token có hợp lệ không
    public boolean isValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUserId(String token) {
        return parseToken(token).getSubject();
    }

    public String getJti(String token) {
        return parseToken(token).getId();
    }

    public Instant getExpiration(String token) {
        return parseToken(token).getExpiration().toInstant();
    }
}
