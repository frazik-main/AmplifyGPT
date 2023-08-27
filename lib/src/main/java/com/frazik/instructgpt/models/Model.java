package com.frazik.instructgpt.models;

import com.frazik.instructgpt.logging.ChatLogger;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Model {

    private final ChatLogger chatLogger = new ChatLogger();
    public abstract int countTokens(List<Map<String, String>> messages);

    public abstract int getTokenLimit();

    public abstract String chat(List<Map<String, String>> messages, Integer maxTokens);

    public String chat(List<Map<String, String>> messages) {
        int tokenCount = countTokens(messages);
        int tokenLimit = getTokenLimit();
        String chatResult = chat(messages, tokenLimit - tokenCount);
        chatLogger.write(messages, chatResult);
        return chatResult;
    }
}