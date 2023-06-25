package com.frazik.instructgpt.models;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.ModelType;
import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenAIModel extends Model {
    private static final Logger logger = LoggerFactory.getLogger(OpenAIModel.class);
    private String model;
    private String apiKey;
    private Encoding encoding;

    public OpenAIModel(String model) {
        this.model = model;
        this.apiKey = System.getenv("OPENAI_API_KEY");
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        this.encoding = registry.getEncodingForModel(ModelType.GPT_3_5_TURBO);
    }

    // Converts List<Map<String, String>> to List<ChatMessage>
    private static List<ChatMessage> toChatMessages(List<Map<String, String>> prompt) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        for (Map<String, String> message : prompt) {
            chatMessages.add(new ChatMessage(message.get("role"), message.get("content")));
        }
        return chatMessages;
    }

    public String chat(List<Map<String, String>> messages, Integer maxTokens) {
        return chat(toChatMessages(messages), maxTokens, 0.8);
    }

    public String chat(String[] prompts) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        for (String prompt : prompts) {
            chatMessages.add(new ChatMessage("user", prompt));
        }
        return chat(chatMessages, 100, 0.8);
    }

    @Override
    public String chat(List<ChatMessage> messages, Integer maxTokens, Double temperature) {
        OpenAiService openAiService = new OpenAiService(apiKey, Duration.ofSeconds(55));

        int numRetries = 3;
        for (int i = 0; i < numRetries; i++) {
            try {
                ChatCompletionRequest chatCompletionRequest = new ChatCompletionRequest();
                chatCompletionRequest.setModel(this.model);
                chatCompletionRequest.setTemperature(temperature);
                chatCompletionRequest.setMessages(messages);
                chatCompletionRequest.setMaxTokens(maxTokens);
                openAiService.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage().getContent();
                return openAiService.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage().getContent();
            } catch (OpenAiHttpException e) {
                if (e.statusCode == 429) {
                    logger.warn("Rate limit exceeded. Retrying after 20 seconds.");
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    continue;
                } else {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException("Failed to get a response from OpenAI API.");
    }

    @Override
    public int countTokens(List<Map<String, String>> messages) {
        int tokensPerMessage, tokensPerName;
        if (model.equals("gpt-3.5-turbo")) {
            tokensPerMessage = 4;
            tokensPerName = -1;
        } else if (model.equals("gpt-4")) {
            tokensPerMessage = 3;
            tokensPerName = 1;
        } else {
            throw new RuntimeException("Unsupported model type: " + model);
        }

        int numTokens = 0;
        for (Map<String, String> message : messages) {
            numTokens += tokensPerMessage;
            for (Map.Entry<String, String> entry : message.entrySet()) {
                numTokens += this.encoding.encode(entry.getValue()).size();
                if (entry.getKey().equals("name")) {
                    numTokens += tokensPerName;
                }
            }
        }
        numTokens += 3;
        return numTokens;
    }

    @Override
    public int getTokenLimit() {
        if (model.equals("gpt-3.5-turbo")) {
            return 3700; //TODO: Token limit should be 4000, there is a bug in the token counter
        } else if (model.equals("gpt-4")) {
            return 8000;
        } else {
            throw new RuntimeException("Unsupported model type: " + model);
        }
    }

}
