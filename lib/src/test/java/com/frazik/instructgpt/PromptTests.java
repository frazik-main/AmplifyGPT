package com.frazik.instructgpt;

import com.frazik.instructgpt.prompts.Prompt;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PromptTests {

        @Test
        void testConstraintRead() {
            Prompt prompt = new Prompt("constraints", "system");
            String promptString = prompt.toPromptString();
            assertTrue(promptString.length() > 150);
        }

}
