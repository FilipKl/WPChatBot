package mk.ukim.finki.wp.chatbotproject.service;

import mk.ukim.finki.wp.chatbotproject.models.Chat;
import mk.ukim.finki.wp.chatbotproject.models.User;

import java.util.List;

public interface ChatService {
    Chat createChat(String title, User user);

    Chat getChatById(Long id, User user);

    List<Chat> getAllChats(User user);

    Chat sendMessage(Long chatId, User user, String userInput);

    void deleteChat(Long chatId, User user);
}



