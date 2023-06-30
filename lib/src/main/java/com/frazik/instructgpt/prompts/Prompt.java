package com.frazik.instructgpt.prompts;

import com.google.gson.Gson;
import org.openqa.selenium.json.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Prompt {
    private final String role;
    private final String content;

    private final Map<String, String> prompts = new HashMap<>();
    
    public Prompt(String key, String role) {
        readPromptJson();
        this.content = readPromptFromJson(key);
        this.role = role;
    }

    public Prompt(String key) {
        readPromptJson();
        this.content = readPromptFromJson(key);
        this.role = null;
    }

    private String readPromptFromJson(String key) {
        return prompts.get(key);
    }

    public String toPromptString() {
        return content;
    }

    public Map<String, String> toPromptMap() {
        Map<String, String> promptMap = new HashMap<>();
        promptMap.put("role", role);
        promptMap.put("content", content);
        return promptMap;
    }
    private void readPromptJson() {
        File file = new File(
                Objects.requireNonNull(this.getClass().
                                getClassLoader().
                                getResource("prompts_en.json"))
                        .getFile()
        );
        try {
            TypeToken<Map<String, List<String>>> token = new TypeToken<Map<String, List<String>>>() {};
            Map<String, List<String>> rawPrompts = new Gson().fromJson(new FileReader(file), token.getType());
            for (Map.Entry<String, List<String>> entry : rawPrompts.entrySet()) {
                String completePromptString = newLineDelimited(entry.getValue());
                prompts.put(entry.getKey(), completePromptString);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String newLineDelimited(List<String> prompt) {
        return String.join("\n", prompt) + "\n";
    }
}
