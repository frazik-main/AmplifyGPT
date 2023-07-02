package com.frazik.instructgpt.prompts;

import com.google.gson.Gson;
import org.openqa.selenium.json.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Prompt {
    private final String role;

    private static Map<String, List<String>> promptsBundle = new HashMap<>();

    static {
        readPromptJson();
    }
    
    public Prompt(String key, String role) {
        readPromptJson();
        this.role = role;
    }

    public static class Builder
    {
        private final List<String> prompts;
        private final String key;
        private final String content;
        private String role;
        public Builder(String key) {
            this.prompts = promptsBundle.get(key);
            this.key = key;
            this.content = null;
            this.role = null;
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
            for (int i = 1; i < prompts.size(); i++) {
                prompts.set(i, "\n" + prompts.get(i));
            }
            return this;
        }

        public Builder withRole(String role) {
            this.role = role;
            return this;
        }

        public Prompt build() {
            return new Prompt(key, role);
        }
    }

    public Prompt(String key) {
        readPromptJson();
        this.role = null;
    }

    public String toPromptString() {
        return content;
    }

    public String toDelimitedPromptString() {
        return newLineDelimited(promptsBundle.get(role));
    }

    public String toFormattedPromptString(Object... args) {
        return String.format(content, args);
    }

    public Map<String, String> toPromptMap() {
        Map<String, String> promptMap = new HashMap<>();
        promptMap.put("role", role);
        promptMap.put("content", content);
        return promptMap;
    }

    private String readPromptFromJson(String key) {
        return newLineDelimited(promptsBundle.get(key));
    }
    private static void readPromptJson() {
        File file = new File(
                Objects.requireNonNull(this.getClass().
                                getClassLoader().
                                getResource("prompts_en.json"))
                        .getFile()
        );
        try {
            TypeToken<Map<String, List<String>>> token = new TypeToken<Map<String, List<String>>>() {};
            promptsBundle = new Gson().fromJson(new FileReader(file), token.getType());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String newLineDelimited(List<String> prompt) {
        return String.join("\n", prompt) + "\n";
    }
}
