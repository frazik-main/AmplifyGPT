package com.frazik.instructgpt.prompts;

import com.google.gson.Gson;
import org.openqa.selenium.json.TypeToken;

import java.io.*;
import java.util.*;

public class Prompt {
    private final String role;
    private final String content;
    private static Map<String, List<String>> promptsBundle = new HashMap<>();

    static {
        readPromptJson();
    }
    
    public Prompt(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getRole() {
        return role;
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
        public Builder withRole(String role) {
            this.role = role;
            return this;
        }

        public Prompt build() {
            String content = String.join("", prompts);
            return new Prompt(role, content);
        }
    }

    private static void readPromptJson() {
        try {
            InputStream inputStream = Prompt.class.getClassLoader().getResourceAsStream("prompts_en.json");

            if (inputStream == null) {
                throw new FileNotFoundException("prompts_en.json file not found.");
            }

            InputStreamReader reader = new InputStreamReader(inputStream);
            TypeToken<Map<String, List<String>>> token = new TypeToken<Map<String, List<String>>>() {};
            promptsBundle = new Gson().fromJson(reader, token.getType());
        } catch (IOException e) {
            throw new RuntimeException("Error reading prompts_en.json file.", e);
        }
    }
}
