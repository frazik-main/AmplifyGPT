package com.frazik.instructgpt;

import com.frazik.instructgpt.embedding.EmbeddingProvider;
import com.frazik.instructgpt.embedding.OpenAIEmbeddingProvider;
import com.frazik.instructgpt.memory.LocalMemory;
import com.frazik.instructgpt.memory.Memory;
import com.frazik.instructgpt.models.Model;
import com.frazik.instructgpt.models.OpenAIModel;
import com.frazik.instructgpt.response.Response;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RegressionTests {

    @Test
    void embeddingTest() {
        EmbeddingProvider embeddingProvider = new OpenAIEmbeddingProvider();
        double[] embeddings = embeddingProvider.get("This is A test");

        assertEquals(-0.006280571, embeddings[0],0.005);
        assertEquals(-0.0040152827, embeddings[2], 0.05);
        assertEquals(0.005179715, embeddings[embeddings.length - 2], 0.05);
    }

    @Test
    void modelTest() {
        Model model = new OpenAIModel("gpt-3.5-turbo");
        List<Map<String, String>> chatMessages = new ArrayList<>();
        chatMessages.add(Map.of("role", "assistant", "content", "This is a test!"));
        String response = model.chat(chatMessages, 100);

        assertTrue(response.length() > 10);
    }

    @Test
    void basicChatTest() {
        String name = "Shop assistant";
        String description = "an AI assistant that researches and finds the best car";

        List<String> goals = new java.util.ArrayList<>();
        goals.add("Search for the car on Google");
        goals.add("Write the list of the top 5 best cars and their prices to a file");
        goals.add("Summarize the pros and cons of each car and write it to a different file called 'cars-research.txt'");
        goals.add("There will be no user assistance. Terminate once writing both files is complete.");

        Agent agent = new Agent(name, description, goals, "gpt-3.5-turbo");
        Response resp = agent.chat();
        assertNotNull(resp);
    }

    @Test
    void basicChatResult() {
        String name = "Shop assistant";
        String description = "an AI assistant that researches and finds the best car";

        List<String> goals = new java.util.ArrayList<>();
        goals.add("Search for the car on Google");
        goals.add("Write the list of the top 5 best cars and their prices to a file");
        goals.add("Summarize the pros and cons of each car and write it to a different file called 'cars-research.txt'");
        goals.add("There will be no user assistance. Terminate once writing both files is complete.");

        Agent agent = new Agent(name, description, goals, "gpt-3.5-turbo");
        Response resp = agent.chat();
        assertEquals("google_search", resp.getCommand());
    }

}
