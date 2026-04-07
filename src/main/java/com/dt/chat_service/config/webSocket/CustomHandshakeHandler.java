package com.dt.chat_service.config.webSocket;

import lombok.NonNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Component
public class CustomHandshakeHandler extends DefaultHandshakeHandler{

    @Override
    protected Principal determineUser(
            @NonNull ServerHttpRequest request,
            @NonNull WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        String userId = (String) attributes.get("userId");

        if (userId != null) {
            return () -> userId; // 🔥 tạo Principal
        }

        return null;
    }

}
