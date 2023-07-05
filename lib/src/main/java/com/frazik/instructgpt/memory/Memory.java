package com.frazik.instructgpt.memory;
import java.util.List;
public abstract class Memory {
    public abstract void add(String doc, String key);
    public abstract List<String> get(String query, int k);
    public abstract void clear();
}
