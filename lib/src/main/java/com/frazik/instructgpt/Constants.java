package com.frazik.instructgpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Credits: Auto-GPT (https://github.com/Significant-Gravitas/Auto-GPT)
 */
public class Constants {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static final List<String> DEFAULT_CONSTRAINTS = Arrays.asList(
            "~4000 word limit for short term memory. Your short term memory is short, so immediately save important information to files.",
            "If you are unsure how you previously did something or want to recall past events, thinking about similar events will help you remember.",
            "No user assistance",
            "Exclusively use a single command listed in double quotes e.g. \"command_name\""
    );
    static String DEFAULT_RESPONSE_FORMAT = "";
    static String DEFAULT_RESPONSE_FORMAT_EXAMPLE = "" +
            "\"thoughts\": {\n" +
            "        \"text\": \"thought\",\n" +
            "        \"reasoning\": \"reasoning\",\n" +
            "        \"plan\": \"- short bulleted\\n- list that conveys\\n- long-term plan\",\n" +
            "        \"criticism\": \"constructive self-criticism\",\n" +
            "        \"speak\": \"thoughts summary to say to user\",\n" +
            "    },\n" +
            "    \"command\": {\"name\": \"command name\", \"args\": {\"arg name\": \"value\"}},\n";
    static final String SEED_INPUT = "Determine which next command to use, and respond using the format specified above:";

    static {
        Map<String, String> thoughtsMap = new HashMap<>();
        thoughtsMap.put("text", "thought");
        thoughtsMap.put("reasoning", "reasoning");
        thoughtsMap.put("plan", "- short bulleted\n- list that conveys\n- long-term plan");
        thoughtsMap.put("criticism", "constructive self-criticism");
        thoughtsMap.put("speak", "thoughts summary to say to user");

        Map<String, String> commandMap = new HashMap<>();
        commandMap.put("name", "command name");

        Map<String, Object> defaultResponseMap = new HashMap<>();
        defaultResponseMap.put("thoughts", thoughtsMap);
        defaultResponseMap.put("command", commandMap);

        try {
            Map<String, Map<String, String>> defaultJSONFormat = objectMapper.readValue(objectMapper.writeValueAsString(defaultResponseMap), HashMap.class);
            String defaultResponseFormatJson = objectMapper.writeValueAsString(DEFAULT_RESPONSE_FORMAT_EXAMPLE);
            DEFAULT_RESPONSE_FORMAT  = String.format("You should only respond in JSON format as described below \nResponse Format: \n%s\nEnsure the response can be parsed by Java JSON ObjectMapper\n\n%s", defaultResponseFormatJson, SEED_INPUT);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    static final List<String> DEFAULT_EVALUATIONS = Arrays.asList(
            "Continuously review and analyze your actions to ensure you are performing to the best of your abilities.",
            "Constructively self-criticize your big-picture behavior constantly.",
            "Reflect on past decisions and strategies to refine your approach.",
            "Every command has a cost, so be smart and efficient. Aim to complete tasks in the least number of steps."
    );

}
