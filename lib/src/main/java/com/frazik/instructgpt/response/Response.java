package com.frazik.instructgpt.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

@Getter
public class Response {
    private final Thought thoughts;
    private final String command;

    private final JsonNode parsedResp;

    private static final ObjectMapper mapper = new ObjectMapper();

    public Response(Thought thoughts, String command, JsonNode parsedResp) {
        this.thoughts = thoughts;
        this.command = command;
        this.parsedResp = parsedResp;
    }

    public boolean hasThoughts() {
        return thoughts != null;
    }

    public boolean hasCommand() {
        return command != null;
    }

    public static Response getResponseFromRaw(String rawResponse) {
        JsonNode parsedResp = loadJson(rawResponse);

        if (parsedResp == null) {
            return null;
        }

        if (parsedResp.has("name")) {
            parsedResp = mapper.createObjectNode().set("command", parsedResp);
        }

        // Parse the 'thoughts' and 'command' parts of the response into objects
        if (parsedResp.has("thoughts") && parsedResp.has("command")) {
            JsonNode thoughtsNode = parsedResp.get("thoughts");
            Thought thoughts = new Thought(
                    thoughtsNode.get("text").asText(),
                    thoughtsNode.get("reasoning").asText(),
                    thoughtsNode.get("plan").asText(),
                    thoughtsNode.get("criticism").asText(),
                    thoughtsNode.get("speak").asText()
            );
            JsonNode commandNode = parsedResp.get("command");
            return new Response(thoughts, commandNode.get("name").asText(), parsedResp);
        }
        return null;
    }

    private static JsonNode loadJson(String s) {
        if (!s.contains("{") || !s.contains("}")) {
            return null;
        }
        try {
            return mapper.readTree(s);
        } catch (Exception exc1) {
            int startIndex = s.indexOf("{");
            int endIndex = s.indexOf("}") + 1;
            String subString = s.substring(startIndex, endIndex);
            try {
                return mapper.readTree(subString);
            } catch (Exception exc2) {
                subString += "}";
                try {
                    return mapper.readTree(subString);
                } catch (Exception exc3) {
                    subString = subString.replace("'", "\"");
                    try {
                        return mapper.readTree(subString);
                    } catch (Exception exc4) {
                        return null;
                    }
                }
            }
        }
    }

}
