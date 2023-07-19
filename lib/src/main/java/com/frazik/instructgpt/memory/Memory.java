package com.frazik.instructgpt.memory;
import java.util.List;
public abstract class Memory {
    public abstract void add(String doc);
    public abstract List<String> get(int k);
    public abstract void clear();
}
