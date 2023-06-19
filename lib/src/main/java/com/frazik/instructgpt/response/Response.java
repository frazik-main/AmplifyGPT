package com.frazik.instructgpt.response;

public class Response {
    private Thought thoughts;
    private String command;

    public Response(Thought thoughts, String command) {
        this.thoughts = thoughts;
        this.command = command;
    }

    public boolean hasThoughts() {
        return thoughts != null;
    }

    public Thought getThoughts() {
        return thoughts;
    }

    public boolean hasCommand() {
        return command != null;
    }

    public String getCommand() {
        return command;
    }

}