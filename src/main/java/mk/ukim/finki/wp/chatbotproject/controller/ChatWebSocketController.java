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


@Controller
public class ChatWebSocketController {

    private final ChatService chatService;
    private final MessageService messageService;

    public ChatWebSocketController(ChatService chatService, MessageService messageService) {
        this.chatService = chatService;
        this.messageService = messageService;
    }

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("User is not authenticated");
        }
        UserServiceImpl.UserDetailsImpl userDetails = (UserServiceImpl.UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUser();
    }


    @MessageMapping("/chat/{chatId}/send")
    @SendTo("/topic/chat/{chatId}")
    public ChatMessageResponse handleChatMessage(
            @DestinationVariable Long chatId,
            ChatMessageRequest request,
            Authentication authentication) {

        try {
            if (request.getContent() == null || request.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("Message content cannot be empty");
            }

            User user = getAuthenticatedUser(authentication);

            chatService.sendMessage(chatId, user, request.getContent());

            Message aiMessage = messageService.getLastAIMessageByChat(chatId);

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



