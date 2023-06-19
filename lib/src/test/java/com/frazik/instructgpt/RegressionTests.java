package com.frazik.instructgpt;

import com.frazik.instructgpt.embedding.EmbeddingProvider;
import com.frazik.instructgpt.embedding.OpenAIEmbeddingProvider;
import com.frazik.instructgpt.memory.Memory;
import com.frazik.instructgpt.memory.LocalMemory;
import com.frazik.instructgpt.models.Model;
import com.frazik.instructgpt.models.OpenAIModel;
import com.frazik.instructgpt.response.Response;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    void memoryTest() {
        EmbeddingProvider embeddingProvider = new OpenAIEmbeddingProvider();
        Memory memory = new LocalMemory(embeddingProvider);

        memory.add("Hello world1", null);
        memory.add("Hello world2", null);

        List<String> rez = memory.get("Hello world1", 1);

        assertEquals(Arrays.asList("Hello world1"), rez);
    }

    @Test
    void modelTest() {
        Model model = new OpenAIModel("gpt-3.5-turbo");
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRole("assistant");
        chatMessage.setContent("This is a test!");
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(chatMessage);
        String response = model.chat(chatMessages, 100, 0.8);

        assertTrue(response.startsWith("As an AI language model"));
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
        assertNull(resp);
    }

}
