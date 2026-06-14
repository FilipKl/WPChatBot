package mk.ukim.finki.wp.chatbotproject.controller;

import mk.ukim.finki.wp.chatbotproject.models.Chat;
import mk.ukim.finki.wp.chatbotproject.models.Message;
import mk.ukim.finki.wp.chatbotproject.models.User;
import mk.ukim.finki.wp.chatbotproject.service.ChatService;
import mk.ukim.finki.wp.chatbotproject.service.MessageService;
import mk.ukim.finki.wp.chatbotproject.service.impl.UserServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;

    public ChatController(ChatService chatService, MessageService messageService) {
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

    @GetMapping("/")
    public String listAllChats(Model model, Authentication authentication) {
        List<Chat> chats = new ArrayList<>();

        if (authentication != null && authentication.isAuthenticated()) {
            User user = getAuthenticatedUser(authentication);
            chats = chatService.getAllChats(user);
        }

        model.addAttribute("chats", chats);
        return "index";
    }

    @PostMapping("/chat/create")
    public String createChat(@RequestParam String title, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Chat newChat = chatService.createChat(title, user);
        return "redirect:/chat/" + newChat.getId();
    }

    @GetMapping("/chat/{chatId}")
    public String openChat(@PathVariable Long chatId, Model model, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Chat chat = chatService.getChatById(chatId, user);
        List<Message> messages = messageService.getMessagesByChat(chatId);

        model.addAttribute("chat", chat);
        model.addAttribute("messages", messages);

        return "chat";
    }

    @PostMapping("/chat/{chatId}/send")
    public String sendMessage(@PathVariable Long chatId, @RequestParam String content, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        chatService.sendMessage(chatId, user, content);
        return "redirect:/chat/" + chatId;
    }


    @PostMapping("/message/{messageId}/edit")
    public String editMessage(@PathVariable Long messageId, @RequestParam String newContent) {
        Message message = messageService.getMessageById(messageId);
        Long chatId = message.getChat().getId();

        messageService.editMessage(messageId, newContent);

        return "redirect:/chat/" + chatId;
    }


    @PostMapping("/chat/{chatId}/delete")
    public String deleteChat(@PathVariable Long chatId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        chatService.deleteChat(chatId, user);
        return "redirect:/";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
}

