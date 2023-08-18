package com.frazik.instructgpt.models;

import com.frazik.instructgpt.logging.ChatLogger;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Model {
    public abstract String chat(List<ChatMessage> messages, Integer maxTokens, Double temperature);

    public abstract int countTokens(List<Map<String, String>> messages);

    public abstract int getTokenLimit();

    public String chat(List<ChatMessage> messages, Integer maxTokens, Double temperature, ChatLogger logger) {
        String response = chat(messages, maxTokens, temperature);
        logger.write(messages, response);
        return response;
    }

}
