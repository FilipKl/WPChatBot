package mk.ukim.finki.wp.chatbotproject.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for incoming WebSocket chat message from client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {
    private Long chatId;
    private String content;
}

