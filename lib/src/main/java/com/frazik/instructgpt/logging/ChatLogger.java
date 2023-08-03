package com.frazik.instructgpt.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ChatLogger {

    private String currentFolder;

    public ChatLogger() {
        this.currentFolder = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
    public void write(List<Map<String, String>> fullPrompt, String response) {

    }

    public void initializeFolder() {

    }

}
