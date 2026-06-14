package mk.ukim.finki.wp.chatbotproject.service.impl;

import mk.ukim.finki.wp.chatbotproject.models.Chat;
import mk.ukim.finki.wp.chatbotproject.models.Message;
import mk.ukim.finki.wp.chatbotproject.models.Role;
import mk.ukim.finki.wp.chatbotproject.models.User;
import mk.ukim.finki.wp.chatbotproject.repository.ChatRepository;
import mk.ukim.finki.wp.chatbotproject.service.ChatService;
import mk.ukim.finki.wp.chatbotproject.service.LLMService;
import mk.ukim.finki.wp.chatbotproject.service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final MessageService messageService;
    private final LLMService llmService;

    public ChatServiceImpl(ChatRepository chatRepository, MessageService messageService, LLMService llmService) {
        this.chatRepository = chatRepository;
        this.messageService = messageService;
        this.llmService = llmService;
    }

    @Override
    @Transactional
    public Chat createChat(String title, User user) {
        Chat chat = new Chat();
        chat.setTitle(title);
        chat.setUser(user);

        return chatRepository.save(chat);
    }

    @Override
    @Transactional(readOnly = true)
    public Chat getChatById(Long id, User user) {
        return chatRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found with ID: " + id + " for user: " + user.getUsername()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Chat> getAllChats(User user) {
        return chatRepository.findAllByUser(user);
    }

    @Override
    @Transactional
    public Chat sendMessage(Long chatId, User user, String userInput) {
        Chat chat = saveChatUserMessage(chatId, user, userInput);

        String aiResponse = llmService.generateResponse(messageService.getMessagesByChat(chatId));

        messageService.saveMessage(chat, Role.AI, aiResponse);

        return getChatById(chatId, user);
    }

    @Transactional
    private Chat saveChatUserMessage(Long chatId, User user, String userInput) {
        Chat chat = getChatById(chatId, user);
        messageService.saveMessage(chat, Role.USER, userInput);
        return chat;
    }

    @Override
    @Transactional
    public void deleteChat(Long chatId, User user) {
        Chat chat = getChatById(chatId, user);
        chatRepository.delete(chat);
    }
}

