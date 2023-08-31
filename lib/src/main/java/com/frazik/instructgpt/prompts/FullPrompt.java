package com.frazik.instructgpt.prompts;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


@Getter
@Setter
public class FullPrompt {

    Map<String, String> prompt;

    public FullPrompt(Map<String, String> prompt) {
        this.prompt = prompt;
    }

}
