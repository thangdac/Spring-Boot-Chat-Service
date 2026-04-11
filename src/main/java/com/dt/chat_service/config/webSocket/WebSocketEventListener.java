package com.dt.chat_service.config.webSocket;

import com.dt.chat_service.dto.response.UserStatusResponse;
import com.dt.chat_service.enums.UserStatus;
import com.dt.chat_service.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)

public class WebSocketEventListener {

    final UserStatusService userStatusService;
    final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleConnect(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = getUserId(accessor);
        if (userId == null) return;

        userStatusService.connect(userId);
        log.info("User connected: {}", userId);

        // Broadcast trạng thái online cho tất cả
        broadcast(userId, UserStatus.ONLINE);

        // 🔥 gửi riêng cho user mới connect
        Set<String> onlineUsers = userStatusService.getOnlineUsers();

        messagingTemplate.convertAndSendToUser(
                userId,
                "/queue/online-users",
                onlineUsers
        );
    }


    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = getUserId(accessor);
        if (userId == null) return;

        userStatusService.disconnect(userId);
        log.info("User disconnected: {}", userId);

        // Broadcast trạng thái offline cho tất cả
        if (!userStatusService.isOnline(userId)) {
            broadcast(userId, UserStatus.OFFLINE);
        }
    }

    private void broadcast(String userId, UserStatus status) {
        messagingTemplate.convertAndSend(
                "/topic/status",
                UserStatusResponse.builder()
                        .userId(userId)
                        .status(status)
                        .build()
        );
    }

    private String getUserId(StompHeaderAccessor accessor) {
        if (accessor.getUser() != null) {
            return accessor.getUser().getName();
        }
        // Fallback: lấy từ session attributes (set trong HandshakeInterceptor)
        Map<String, Object> attrs = accessor.getSessionAttributes();
        if (attrs != null && attrs.containsKey("userId")) {
            return (String) attrs.get("userId");
        }
        return null;
    }


}
