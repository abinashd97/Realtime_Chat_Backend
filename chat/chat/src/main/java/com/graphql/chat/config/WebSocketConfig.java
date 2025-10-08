package com.graphql.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${chat.allowed.origins:*}")
    private String allowedOrigins; // comma separated

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // outbound (subscribe)
        config.setApplicationDestinationPrefixes("/app"); // inbound (send to message mapping)
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String[] origins = allowedOrigins.split(",");
        registry.addEndpoint("/ws-chat")
                .setAllowedOrigins(origins)
                .withSockJS(); // SockJS fallback
    }
}
