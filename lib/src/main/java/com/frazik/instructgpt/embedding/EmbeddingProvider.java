package com.frazik.instructgpt.embedding;

import java.util.HashMap;
import java.util.Map;

public abstract class EmbeddingProvider {
    public abstract double[] get(String text);

    public double[] call(String text) {
        return this.get(text);
    }

    public Map<String, String> config() {
        Map<String, String> map = new HashMap<>();
        map.put("class", this.getClass().getSimpleName());
        map.put("type", "embedding_provider");
        return map;
    }

}