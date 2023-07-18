package com.frazik.instructgpt.memory;

import lombok.extern.slf4j.Slf4j;;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class LocalMemory extends Memory {

    private final List<String> docs;

    public LocalMemory() {
        super();
        this.docs = new ArrayList<>();
    }

    @Override
    public void add(String doc) {
        this.docs.add(0, doc);
    }

    @Override
    public List<String> get(int k) {
        // get last k docs, or all docs if k > docs.size()
        if (k > this.docs.size()) {
            return this.docs;
        }
        return this.docs.subList(0, k);
    }
    @Override
    public void clear() {
        this.docs.clear();
    }

}
