package mk.ukim.finki.wp.chatbotproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket configuration for Spring WebSocket with STOMP protocol.
 * Enables real-time bidirectional communication between client and server.
 * Includes authentication interceptor for extracting user context from WebSocket messages.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configure the WebSocket endpoints.
     * Clients will connect to /ws endpoint.
     * Fallback to SockJS for browsers that don't support WebSocket.
     *
     * @param registry the STOMP endpoint registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    /**
     * Configure the message broker.
     * - /app: prefix for messages from clients to server (@MessageMapping)
     * - /topic: prefix for broadcasting messages to multiple clients
     * - /queue: prefix for point-to-point messages to specific users
     *
     * @param config the message broker registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Register the WebSocket authentication interceptor on the inbound channel.
     * This extracts the authenticated Principal from the StompHeaderAccessor
     * and stores it in the WebSocket session attributes.
     *
     * @param registration the channel registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new WebSocketAuthInterceptor());
    }
}


