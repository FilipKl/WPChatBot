package mk.ukim.finki.wp.chatbotproject.service;

import mk.ukim.finki.wp.chatbotproject.models.Message;

import java.util.List;

public interface LLMService {

    String generateResponse(List<Message> messages);
    String generateResponse(List<Message> messages, KnowledgeTools knowledgeTools);
}
