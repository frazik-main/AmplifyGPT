package com.frazik.instructgpt.memory;

import com.frazik.instructgpt.embedding.EmbeddingProvider;
import lombok.extern.slf4j.Slf4j;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.*;


@Slf4j
public class LocalMemory extends Memory {

    private final List<String> docs;
    private INDArray embs;
    private final EmbeddingProvider embeddingProvider;

    public LocalMemory(EmbeddingProvider embeddingProvider) {
        super();
        this.docs = new ArrayList<>();
        this.embs = null;
        this.embeddingProvider = embeddingProvider;
    }

    @Override
    public void add(String doc, String key) {
        if (key == null) {
            key = doc;
        }
        double[] embeddings = this.embeddingProvider.get(key);
        INDArray emb = Nd4j.create(embeddings);

        if (this.embs == null) {
            this.embs = Nd4j.expandDims(emb, 0);
        } else {
            this.embs = Nd4j.concat(0, this.embs, Nd4j.expandDims(emb, 0));
        }
        this.docs.add(doc);
    }

    @Override
    public List<String> get(String query, int k) {
        if (this.embs == null) {
            return new ArrayList<String>();
        }
        double[] embeddings = embeddingProvider.get(query);
        INDArray scores;
        try (INDArray emb = Nd4j.create(embeddings)) {

            scores = this.embs.mmul(emb);
        }
        int[] idxs = Nd4j.argMax(scores, 0).toIntVector();
        String[] results = new String[k];
        for (int i = 0; i < k; i++) {
            results[i] = this.docs.get(idxs[i]);
        }
        return Arrays.asList(results);
    }
    @Override
    public void clear() {
        this.docs.clear();
        this.embs = null;
    }

}
