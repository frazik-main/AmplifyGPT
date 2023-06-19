package com.frazik.instructgpt.embedding;

import com.theokanning.openai.embedding.Embedding;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.service.OpenAiService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OpenAIEmbeddingProvider extends EmbeddingProvider {
    private final String model;

    public OpenAIEmbeddingProvider(String model) {
        super();
        this.model = model;
    }

    public OpenAIEmbeddingProvider() {
        this("text-embedding-ada-002");
    }

    @Override
    public double[] get(String text) {
        String token = System.getenv("OPENAI_API_KEY");
        OpenAiService  openAiService = new OpenAiService(token);
        EmbeddingRequest embeddingRequest = new EmbeddingRequest();
        embeddingRequest.setModel(model);
        embeddingRequest.setInput(Collections.singletonList(text));
        EmbeddingResult embeddingResult = openAiService.createEmbeddings(embeddingRequest);

        List<Embedding> embeddings = embeddingResult.getData();
        List<Double> firstEmbeddings = embeddings.get(0).getEmbedding();
        return firstEmbeddings.stream().mapToDouble(Double::doubleValue).toArray();
    }

    @Override
    public Map<String, String> config() {
        Map<String, String> map = super.config();
        map.put("model", this.model);
        return map;
    }

    public static OpenAIEmbeddingProvider fromConfig(Map<String, String> config) {
        return new OpenAIEmbeddingProvider(config.get("model"));
    }
}