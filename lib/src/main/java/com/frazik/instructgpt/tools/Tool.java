package com.frazik.instructgpt.tools;

import com.frazik.instructgpt.Agent;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Tool {
    public String getId() {
        return String.join("_", camelCaseSplit(this.getClass().getSimpleName())).toLowerCase();
    }

    public String getDesc() {
        return String.join(" ", camelCaseSplit(this.getClass().getSimpleName()));
    }

    public abstract Map<String, String> getArgs();

    public abstract Map<String, Object> run(Map<String, String> kwargs);

    public abstract Map<String, String> getResp();

    private Agent agent;

    public String prompt() {
        Map<String, Object> promptMap = new HashMap<>();
        promptMap.put("name", this.getId());
        promptMap.put("description", this.getDesc());
        promptMap.put("args", this.getArgs());
        promptMap.put("response_format", this.getResp());
        return new Gson().toJson(promptMap);
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    private List<String> camelCaseSplit(String str) {
        List<String> words = new ArrayList<>();
        Matcher matcher = Pattern.compile("[A-Z](?:[a-z]+|[A-Z]*(?=[A-Z]|$))").matcher(str);
        while (matcher.find()) {
            words.add(matcher.group());
        }
        return words;
    }
}

