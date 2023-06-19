package com.frazik.instructgpt.response;

public class Thought {
    private String text;
    private String reasoning;
    private String plan;
    private String criticism;
    private String speak;

    public Thought(String text, String reasoning, String plan, String criticism, String speak) {
        this.text = text;
        this.reasoning = reasoning;
        this.plan = plan;
        this.criticism = criticism;
        this.speak = speak;
    }

    public boolean hasText() {
        return text != null;
    }

    public String getText() {
        return text;
    }

    public boolean hasReasoning() {
        return reasoning != null;
    }

    public String getReasoning() {
        return reasoning;
    }

    public boolean hasPlan() {
        return plan != null;
    }

    public String getPlan() {
        return plan;
    }

    public boolean hasCriticism() {
        return criticism != null;
    }

    public String getCriticism() {
        return criticism;
    }

    public boolean hasSpeak() {
        return speak != null;
    }

    public String getSpeak() {
        return speak;
    }

}
