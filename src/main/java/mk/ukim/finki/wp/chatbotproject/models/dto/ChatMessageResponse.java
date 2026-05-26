package mk.ukim.finki.wp.chatbotproject.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wp.chatbotproject.models.Role;

import java.time.LocalDateTime;

/**
 * DTO for outgoing WebSocket chat message to client.
 * Contains message details to be displayed in real-time.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
    private Long messageId;
    private Long chatId;
    private String content;
    private Role role;
    private LocalDateTime timestamp;
    private Boolean edited;
}

