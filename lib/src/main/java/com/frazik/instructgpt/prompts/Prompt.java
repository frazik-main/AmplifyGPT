package com.frazik.instructgpt.prompts;

import com.google.gson.Gson;
import org.openqa.selenium.json.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Prompt {
    private final String role;
    private String content;
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

    public static class Builder
    {
        private final List<String> prompts;
        private String role;
        public Builder(String key) {
            this.prompts = promptsBundle.get(key);
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
        File file = new File(
                Objects.requireNonNull(Prompt.class.
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
}
