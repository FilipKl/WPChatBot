package mk.ukim.finki.wp.chatbotproject.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;

public class WebSocketAuthInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        Authentication authentication = (Authentication) accessor.getUser();

        if (authentication != null) {
            accessor.getSessionAttributes().put("user", authentication);
        }

        return message;
    }
}

