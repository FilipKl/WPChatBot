package mk.ukim.finki.wp.chatbotproject.service;

import mk.ukim.finki.wp.chatbotproject.models.Message;

import java.util.List;
import java.util.function.Consumer;

public interface LLMService {
    String generateResponse(List<Message> messages);
}
