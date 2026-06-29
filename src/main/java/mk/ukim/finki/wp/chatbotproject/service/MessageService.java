package mk.ukim.finki.wp.chatbotproject.service;

import mk.ukim.finki.wp.chatbotproject.models.Chat;
import mk.ukim.finki.wp.chatbotproject.models.Message;
import mk.ukim.finki.wp.chatbotproject.models.Role;

import java.util.List;


public interface MessageService {

    Message saveMessage(Chat chat, Role role, String content);

    List<Message> getMessagesByChat(Long chatId);

    Message editMessage(Long messageId, String newContent);

    Message getMessageById(Long messageId);

    Message getLastAIMessageByChat(Long chatId);
}

