package com.frazik.instructgpt.prompts;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PromptHistory {

    private final List<Map<String, String>> values;

    public PromptHistory() {
        this.values = new ArrayList<>();
    }

    public int getSize() {
        return values.size();
    }

    public Map<String, String> getValue(int i) {
        return values.get(i);
    }

    public void clear() {
        values.clear();
    }

    public void remove(int i) {
        this.values.remove(i);
    }

    public String subListToString(int start, int end) {
        return listToString(values.subList(start, end));
    }

    public void addNewPrompt(String role, String content) {
        this.values.add(PromptUtils.buildPrompts(role, content));
    }

    public static String listToString(List<Map<String, String>> values) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (Map<String, String> map : values) {
            sb.append("  {\n");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append("    ").append(key).append(": ").append(value).append(",\n");
            }
            sb.append("  },\n");
        }

        sb.append("]\n");

        return sb.toString();
    }

}
