package mk.ukim.finki.wp.chatbotproject.controller;

import mk.ukim.finki.wp.chatbotproject.models.Message;
import mk.ukim.finki.wp.chatbotproject.models.User;
import mk.ukim.finki.wp.chatbotproject.models.dto.ChatMessageRequest;
import mk.ukim.finki.wp.chatbotproject.models.dto.ChatMessageResponse;
import mk.ukim.finki.wp.chatbotproject.service.ChatService;
import mk.ukim.finki.wp.chatbotproject.service.MessageService;
import mk.ukim.finki.wp.chatbotproject.service.impl.UserServiceImpl;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

/**
 * WebSocket controller for real-time chat messages.
 * Handles incoming WebSocket messages and broadcasts responses.
 * Only authenticated users can send messages.
 */
@Controller
public class ChatWebSocketController {

    private final ChatService chatService;
    private final MessageService messageService;

    public ChatWebSocketController(ChatService chatService, MessageService messageService) {
        this.chatService = chatService;
        this.messageService = messageService;
    }

    /**
     * Extract authenticated user from the security context.
     *
     * @param authentication the Authentication object from Spring Security
     * @return the User entity
     */
    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("User is not authenticated");
        }
        UserServiceImpl.UserDetailsImpl userDetails = (UserServiceImpl.UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUser();
    }

    /**
     * Handle incoming chat messages via WebSocket.
     * Processes user message and generates AI response in real-time.
     *
     * Message flow:
     * 1. Client sends to /app/chat/{chatId}/send
     * 2. Spring routes to this method with @MessageMapping
     * 3. Method processes and broadcasts to /topic/chat/{chatId}
     * 4. All connected clients receive the response
     *
     * @param chatId the ID of the chat
     * @param request the incoming message request
     * @param authentication the authenticated user
     * @return the chat message response (sent to all subscribers)
     */
    @MessageMapping("/chat/{chatId}/send")
    @SendTo("/topic/chat/{chatId}")
    public ChatMessageResponse handleChatMessage(
            @DestinationVariable Long chatId,
            ChatMessageRequest request,
            Authentication authentication) {

        try {
            // Validate the request
            if (request.getContent() == null || request.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("Message content cannot be empty");
            }

            // Get authenticated user
            User user = getAuthenticatedUser(authentication);

            // Send the message through ChatService (saves user message and generates AI response)
            chatService.sendMessage(chatId, user, request.getContent());

            // Get the last AI response message
            Message aiMessage = messageService.getLastAIMessageByChat(chatId);

            // Convert to response DTO
            return convertMessageToResponse(aiMessage);

        } catch (Exception e) {
            ChatMessageResponse errorResponse = new ChatMessageResponse();
            errorResponse.setChatId(chatId);
            errorResponse.setContent("Error: " + e.getMessage());
            errorResponse.setRole(mk.ukim.finki.wp.chatbotproject.models.Role.AI);
            errorResponse.setTimestamp(java.time.LocalDateTime.now());
            errorResponse.setEdited(false);
            return errorResponse;
        }
    }

    /**
     * Convert Message entity to ChatMessageResponse DTO.
     *
     * @param message the message entity
     * @return the response DTO
     */
    private ChatMessageResponse convertMessageToResponse(Message message) {
        return new ChatMessageResponse(
                message.getId(),
                message.getChat().getId(),
                message.getContent(),
                message.getRole(),
                message.getTimestamp(),
                message.getEdited()
        );
    }
}



