package com.frazik.instructgpt;

import com.frazik.instructgpt.prompts.Prompt;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PromptTests {

        @Test
        void testPromptLoad() {
            Prompt prompt = new Prompt.Builder("constraints").build();
            String promptString = prompt.getContent();
            assertTrue(promptString.length() > 150);
        }

        @Test
        void testPromptLoadWithNumberList() {
            Prompt prompt = new Prompt.Builder("constraints").numberList().build();
            String promptString = prompt.getContent();
            assertTrue(promptString.contains("1."));
        }

        @Test
        void testPromptLoadWithDelimited() {
            Prompt prompt = new Prompt.Builder("constraints").delimited().build();
            String promptString = prompt.getContent();
            int countNewLines = promptString.length() - promptString.replace("\n", "").length();
            assertTrue(countNewLines > 3);
        }

        @Test
        void testPromptLoadWithFormatted() {
            Prompt prompt = new Prompt.Builder("persona").formatted(0, "test123", "test456").build();
            String promptString = prompt.getContent();
            assertTrue(promptString.contains("test123"));
        }

}
