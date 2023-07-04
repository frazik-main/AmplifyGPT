package com.frazik.instructgpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Credits: Auto-GPT (<a href="https://github.com/Significant-Gravitas/Auto-GPT">...</a>)
 */
public class Constants {
    private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    static final String SEED_INPUT = "Determine which next command to use, and respond using the format specified above:";

    public static String getDefaultResponseFormat() {
        Map<String, String> thoughtsMap = new HashMap<>();
        thoughtsMap.put("text", "thought");
        thoughtsMap.put("reasoning", "reasoning");
        thoughtsMap.put("plan", "- short bulleted\n- list that conveys\n- long-term plan");
        thoughtsMap.put("criticism", "constructive self-criticism");
        thoughtsMap.put("speak", "thoughts summary to say to user");

        Map<String, String> commandMap = new HashMap<>();
        commandMap.put("name", "command name");
        commandMap.put("args", "{\"arg name\": \"value\"}");

        Map<String, Object> defaultResponseMap = new HashMap<>();
        defaultResponseMap.put("thoughts", thoughtsMap);
        defaultResponseMap.put("command", commandMap);

        try {
            String defaultJSONFormat = objectMapper.writeValueAsString(defaultResponseMap);
            return String.format("You should only respond in JSON format as described below \nResponse Format: \n%s\nEnsure the response can be parsed by Java JSON ObjectMapper\n\n%s", defaultJSONFormat, SEED_INPUT);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
