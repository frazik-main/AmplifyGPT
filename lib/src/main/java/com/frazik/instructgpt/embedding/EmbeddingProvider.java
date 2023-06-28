package com.frazik.instructgpt.embedding;


public abstract class EmbeddingProvider {
    public abstract double[] get(String text);

}