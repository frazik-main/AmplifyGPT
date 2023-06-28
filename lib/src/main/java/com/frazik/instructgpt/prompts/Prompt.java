package com.frazik.instructgpt.prompts;

import com.google.gson.Gson;
import org.openqa.selenium.json.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Prompt {
    protected String role;
    protected String content;

    private Map<String, String> prompts = new HashMap<>();
    
    public Prompt() {
        readPromptJson();
    }

    protected String readPromptFromJson(String key) {
        return prompts.get(key);
    }

    public String toPromptString() {
        return content + "\n";
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
                StringBuilder completePrompt = new StringBuilder();
                for(String p : entry.getValue()) {
                    completePrompt.append(p);
                }
                prompts.put(entry.getKey(), completePrompt.toString());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
