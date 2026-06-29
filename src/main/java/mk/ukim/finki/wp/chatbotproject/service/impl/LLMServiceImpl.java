package mk.ukim.finki.wp.chatbotproject.service.impl;

import mk.ukim.finki.wp.chatbotproject.models.Message;
import mk.ukim.finki.wp.chatbotproject.models.Role;
import mk.ukim.finki.wp.chatbotproject.service.KnowledgeTools;
import mk.ukim.finki.wp.chatbotproject.service.LLMService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;


@Service
public class LLMServiceImpl implements LLMService {

    private final ChatClient chatClient;

    public LLMServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public String generateResponse(List<Message> messages) {
        String conversationContext = buildConversationContext(messages);

        return chatClient.prompt()
                .user(conversationContext)
                .call()
                .content();
    }

    @Override
    public String generateResponse(List<Message> messages, KnowledgeTools knowledgeTools) {
        String conversationContext = buildConversationContext(messages);

        String lastUserMessage = messages.stream()
                .filter(m -> m.getRole() == Role.USER)
                .reduce((first, second) -> second)
                .map(Message::getContent)
                .orElse("");

        String knowledgeResult = knowledgeTools.searchKnowledge(lastUserMessage);

        String systemPrompt;
        if (!knowledgeResult.equals("No relevant knowledge found.")) {
            systemPrompt = "You are a helpful assistant. " +
                    "A human-verified answer exists for this question. " +
                    "You MUST respond with exactly this answer, do not add or change anything:\n\n" +
                    knowledgeResult;
        } else {
            systemPrompt = "You are a helpful assistant.";
        }

        return chatClient.prompt()
                .system(systemPrompt)
                .user(conversationContext)
                .call()
                .content();
    }

    private String buildConversationContext(List<Message> messages) {
        StringBuilder contextBuilder = new StringBuilder();
        for (Message msg : messages) {
            if (msg.getRole() == Role.USER) {
                contextBuilder.append("User: ").append(msg.getContent()).append("\n");
            } else if (msg.getRole() == Role.AI) {
                contextBuilder.append("Assistant: ").append(msg.getContent()).append("\n");
            }
        }
        contextBuilder.append("Assistant: ");

        return contextBuilder.toString();
    }
}
