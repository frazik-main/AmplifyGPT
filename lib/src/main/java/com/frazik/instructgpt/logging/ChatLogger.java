package com.frazik.instructgpt.logging;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Slf4j
public class ChatLogger {

    private final File currentFolder;
    private int currentChatIndex = 0;
    private static final SimpleDateFormat CURRENT_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private static final SimpleDateFormat CURRENT_MIN_SEC_FORMAT = new SimpleDateFormat("mm-ss");
    public ChatLogger() {
        this.currentFolder = new File("chat-logs/" + System.getProperty("sun.java.command"));
        if (!currentFolder.exists()) {
            currentFolder.mkdirs();
        }
    }

    public void write(String[] prompts, String response) {
        currentChatIndex++;
        // get current millis
        String currentMinSec = CURRENT_MIN_SEC_FORMAT.format(new Date());
        File promptFile = new File(currentFolder, "sum-prompt." + currentChatIndex + ".log");
        File reponseFile = new File(currentFolder,"sum-response." + currentChatIndex + ".log");
        try {
            // Write full prompt to prompt.log
            if (!promptFile.createNewFile()) {
                return;
            };

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

    public void write(List<Map<String, String>> fullPrompt, String response) {
        currentChatIndex++;
        // get current millis
        String currentMinSec = CURRENT_MIN_SEC_FORMAT.format(new Date());
        File promptFile = new File(currentFolder, "plain-prompt." + currentChatIndex + ".log");
        File reponseFile = new File(currentFolder,"plain-response." + currentChatIndex + ".log");
        try {
            // Write full prompt to prompt.log
            if (!promptFile.createNewFile()) {
                return;
            };

            for (Map<String, String> prompt : fullPrompt) {
                for (Map.Entry<String, String> entry : prompt.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    FileUtils.write(promptFile, key + ": " + value, "UTF-8", true);
                }
            }
            // Write response to response.log
            if (reponseFile.createNewFile()) {
                FileUtils.write(reponseFile, response, "UTF-8");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
