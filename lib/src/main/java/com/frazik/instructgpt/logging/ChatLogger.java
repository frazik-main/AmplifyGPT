package com.frazik.instructgpt.logging;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ChatLogger {

    private String currentFolder;

    public ChatLogger() {
        this.currentFolder = new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date());
    }
    public void write(List<Map<String, String>> fullPrompt, String response) {
        // get current millis
        int currentMillis = (int) System.currentTimeMillis();
        File promptFile = new File(currentFolder + "/prompt" + currentMillis + ".log");
        File reponseFile = new File(currentFolder + "/response" + currentMillis + ".log");
        try {
            // Write full prompt to prompt.log
            promptFile.createNewFile();
            for (Map<String, String> prompt : fullPrompt) {
                for (Map.Entry<String, String> entry : prompt.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("speaker")) {
                        value = value + ": ";
                    }
                    if (key.equals("text")) {
                        value = value + "\n";
                    }
                    FileUtils.write(promptFile, value);
                }
            }
            // Write response to response.log
            reponseFile.createNewFile();
            FileUtils.write(reponseFile, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initializeFolder() {
        File file = new File(currentFolder);
        file.mkdir();
    }

}
