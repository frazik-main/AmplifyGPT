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
    private int currentChatIndex = 0;
    public ChatLogger() {
        this.currentFolder = new File("chat-logs/" + System.getProperty("sun.java.command"));
        if (!currentFolder.exists()) {
            currentFolder.mkdirs();
        }
    }
    public void write(List<Map<String, String>> fullPrompt, String response) {
        currentChatIndex++;
        File promptFile = getAndInitialize("plain-prompt.");
        File reponseFile = getAndInitialize("plain-response.");
        try {
            // Write full prompt to prompt.log
            if (!promptFile.createNewFile()) {
                return;
            }

            for (Map<String, String> prompt : fullPrompt) {
                for (Map.Entry<String, String> entry : prompt.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    FileUtils.write(promptFile, key + ": " + value + "\n", "UTF-8", true);
                }
            }

            // Write response to response.log
            if (reponseFile.createNewFile()) {
                FileUtils.write(reponseFile, response, "UTF-8");
            }
            FileUtils.write(promptFile, "\n--", "UTF-8", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(String[] prompts, String response) {
        currentChatIndex++;
        // get current millis
        File promptFile = getAndInitialize("sum-prompt.");
        File reponseFile = getAndInitialize("sum-response.");
        try {
            // Write full prompt to prompt.log
            if (!promptFile.createNewFile()) {
                return;
            }

            for (String prompt : prompts) {
                FileUtils.write(promptFile, prompt, "UTF-8", true);
            }

            // Write response to response.log
            if (reponseFile.createNewFile()) {
                FileUtils.write(reponseFile, response, "UTF-8");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File getAndInitialize(String x) {
        File promptFile = new File(currentFolder, x + currentChatIndex + ".log");
        if (promptFile.exists()) {
            promptFile.delete();
        }
        return promptFile;
    }

}
