package com.dt.chat_service.config.webSocket;

import com.dt.chat_service.Security.JwtTokenProvider;
import com.dt.chat_service.service.TokenBlacklistService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

     final JwtTokenProvider jwtTokenProvider;
     final TokenBlacklistService tokenBlacklistService;

    @Override
    public boolean beforeHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {

            // 🔥 lấy token từ query: /ws?token=xxx
            String token = servletRequest.getServletRequest().getParameter("token");

            if (token == null || !jwtTokenProvider.isValid(token)) {
                return false;
            }

            if (tokenBlacklistService.isBlacklisted(jwtTokenProvider.getJti(token))) {
                return false;
            }

            // 🔥 lưu userId
            String userId = jwtTokenProvider.getUserId(token);
            attributes.put("userId", userId);

            System.out.println("WS CONNECT userId = " + userId);
            return true;
        }

        return false;
    }

    @Override
    public void afterHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            Exception exception) {
    }

}
