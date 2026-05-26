package mk.ukim.finki.wp.chatbotproject.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;

/**
 * WebSocket channel interceptor for extracting authenticated Principal from WebSocket messages.
 * Stores the authenticated user in the WebSocket session for later use in handlers.
 */
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    /**
     * Intercept incoming WebSocket messages before they are processed.
     * Extracts the authenticated Principal from the STOMP header and stores it in session attributes.
     *
     * @param message the incoming message
     * @param channel the message channel
     * @return the processed message
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // Extract the authenticated Principal from the security context
        Authentication authentication = (Authentication) accessor.getUser();

        if (authentication != null) {
            // Store the authenticated user in session attributes for later retrieval
            accessor.getSessionAttributes().put("user", authentication);
        }

        return message;
    }
}

