package mk.ukim.finki.wp.chatbotproject.service;

import mk.ukim.finki.wp.chatbotproject.models.Chat;
import mk.ukim.finki.wp.chatbotproject.models.User;

import java.util.List;

/**
 * Service interface for Chat operations.
 */
public interface ChatService {

    /**
     * Create a new chat with the given title for a user.
     *
     * @param title the title of the chat
     * @param user the User creating the chat
     * @return the created Chat entity
     */
    Chat createChat(String title, User user);

    /**
     * Retrieve a chat by ID for a specific user.
     *
     * @param id the ID of the chat
     * @param user the User requesting the chat
     * @return the Chat entity
     * @throws IllegalArgumentException if not found or does not belong to user
     */
    Chat getChatById(Long id, User user);

    /**
     * Retrieve all chats for a specific user.
     *
     * @param user the User requesting their chats
     * @return list of Chat entities owned by the user
     */
    List<Chat> getAllChats(User user);

    /**
     * Send a message in a chat for a specific user.
     * This method:
     * 1. Saves the USER message to the database
     * 2. Generates an AI response using the full conversation history
     * 3. Saves the AI response to the database
     *
     * @param chatId the ID of the chat
     * @param user the User sending the message
     * @param userInput the user message content
     * @return the Chat entity with updated messages
     * @throws IllegalArgumentException if chat not found or does not belong to user
     */
    Chat sendMessage(Long chatId, User user, String userInput);

    /**
     * Delete a chat and all its associated messages for a specific user.
     *
     * @param chatId the ID of the chat to delete
     * @param user the User deleting the chat
     * @throws IllegalArgumentException if chat not found or does not belong to user
     */
    void deleteChat(Long chatId, User user);
}



