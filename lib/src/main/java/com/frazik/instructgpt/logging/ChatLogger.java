package com.frazik.instructgpt.logging;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@SuppressWarnings("ResultOfMethodCallIgnored")
@Slf4j
public class ChatLogger {

    private final File currentFolder;
    private final File promptFile;
    private final File summaryFile;
    public ChatLogger() {
        this.currentFolder = new File("chat-logs/" + System.getProperty("sun.java.command"));
        this.promptFile = new File(currentFolder, "prompt.log");
        this.summaryFile = new File(currentFolder, "summary.log");
        if (promptFile.exists()) {
            promptFile.delete();
        }
        if (summaryFile.exists()) {
            summaryFile.delete();
        }
        if (!currentFolder.exists()) {
            currentFolder.mkdirs();
        }
    }
    public void write(List<Map<String, String>> fullPrompt, String response) {
        try {
            FileUtils.write(promptFile, "\n--prompt--\n", "UTF-8", true);
            for (Map<String, String> prompt : fullPrompt) {
                for (Map.Entry<String, String> entry : prompt.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    FileUtils.write(promptFile, key + ": " + value + "\n", "UTF-8", true);
                }
            }
            FileUtils.write(promptFile, "\n--response--\n", "UTF-8", true);
            FileUtils.write(promptFile, response, "UTF-8", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(String[] prompts, String response) {
        try {
            FileUtils.write(summaryFile, "\n--prompt--\n", "UTF-8", true);
            for (String prompt : prompts) {
                FileUtils.write(summaryFile, prompt, "UTF-8", true);
            }
            FileUtils.write(summaryFile, "\n--response--\n", "UTF-8", true);
            // Write response to response.log
            FileUtils.write(summaryFile, response, "UTF-8", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File getAndInitialize(String x) {
        File promptFile = new File(currentFolder, x + ".log");
        if (promptFile.exists()) {
            promptFile.delete();
        }
        return promptFile;
    }

}
