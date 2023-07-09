package com.frazik.instructgpt.prompts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import org.openqa.selenium.json.TypeToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prompt {
    private final String role;
    private final String content;
    private static final Map<String, List<String>> promptsBundle;
    private static final Map<String, Map<String, String>> defaultResponsesJson;
    private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    static {
        // Use generic types to prepare for future expansion
        TypeToken<Map<String, List<String>>> promptsToken = new TypeToken<Map<String, List<String>>>() {};
        promptsBundle = readPromptJson(promptsToken, "prompts_en.json");
        // Use generic types to prepare for future expansion
        TypeToken<Map<String, Map<String, String>>> defaultResponsesToken =
                new TypeToken<Map<String, Map<String, String>>>() {};
        defaultResponsesJson = readPromptJson(defaultResponsesToken, "default_response_en.json");
    }
    
    public Prompt(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public static String getDefaultResponse() {
        try {
            return objectMapper.writeValueAsString(defaultResponsesJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public Map<String, String> getPrompt() {
        Map<String, String> prompt = new HashMap<>();
        prompt.put("role", role);
        prompt.put("content", content);
        return prompt;
    }

    public static class Builder
    {
        private final List<String> prompts;
        private String role;
        public Builder(String key) {
            //deep copy of prompts
            prompts = new ArrayList<>(promptsBundle.get(key));
        }

        public Builder numberList() {
            // Append a number to each prompt
            for (int i = 1; i < prompts.size(); i++) {
                prompts.set(i, i + ". " + prompts.get(i));
            }
            return this;
        }

        public Builder delimited() {
            // Append a newline to each prompt
            prompts.replaceAll(s -> s + "\n");
            return this;
        }

        public Builder formatted(int i, Object... args) {
            // Format the prompt
            prompts.set(i, String.format(prompts.get(i), args));
            return this;
        }

        public Builder formattedWithCurrentTime(int i) {
            String currentTime  = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy"));
            return formatted(i, currentTime);
        }
        public Builder withRole(String role) {
            this.role = role;
            return this;
        }

        public Prompt build() {
            String content = String.join("", prompts);
            return new Prompt(role, content);
        }
    }

    private static <T> T readPromptJson(TypeToken<T> token, String jsonFileName) {
        try {
            InputStream inputStream = Prompt.class.getClassLoader().getResourceAsStream(jsonFileName);

            if (inputStream == null) {
                throw new FileNotFoundException(jsonFileName + " file not found.");
            }

            InputStreamReader reader = new InputStreamReader(inputStream);
            return new Gson().fromJson(reader, token.getType());
        } catch (IOException e) {
            throw new RuntimeException("Error reading " + jsonFileName, e);
        }
    }
}
