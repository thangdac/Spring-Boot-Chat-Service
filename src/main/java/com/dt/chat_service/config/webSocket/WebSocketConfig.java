package com.dt.chat_service.config.webSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    final WebSocketAuthInterceptor authInterceptor;
    final CustomHandshakeHandler handshakeHandler;


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .addInterceptors(authInterceptor) // 🔥 check JWT
                .setHandshakeHandler(handshakeHandler) // 🔥 set Principal
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // 🔥 client subscribe
        registry.enableSimpleBroker("/topic", "/queue");

        // 🔥 client gửi lên
        registry.setApplicationDestinationPrefixes("/app");

        // 🔥 gửi riêng user
        registry.setUserDestinationPrefix("/user");
    }
}
