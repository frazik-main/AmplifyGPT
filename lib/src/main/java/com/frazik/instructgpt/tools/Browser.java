package com.frazik.instructgpt.tools;

import com.frazik.instructgpt.Summarizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Browser extends Tool {
    private ChromeOptions options;
    private WebDriver driver;
    private Summarizer summarizer;

    public Browser() {
        super();
        options = new ChromeOptions();
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.5615.49 Safari/537.36");
        // options.setHeadless(true);
        summarizer = new Summarizer("model");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                close();
            }
        });
    }

    private void initDriver() {
        close();
        driver = new ChromeDriver(options);
    }

    private String get(String url) {
        if (driver == null) {
            initDriver();
        }
        try {
            driver.get(url);
        } catch (NoSuchWindowException e) {
            initDriver();
            driver.get(url);
        }
        Duration duration = Duration.ofMillis(10000);
        WebDriverWait wait = new WebDriverWait(driver, duration);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript("return document.body.outerHTML;").toString();
    }

    private List<Map<String, String>> extractLinksFromSoup(Document soup) {
        List<Map<String, String>> links = new ArrayList<Map<String, String>>();
        Elements linkElements = soup.select("a[href]");
        for (Element linkElement : linkElements) {
            Map<String, String> link = new HashMap<String, String>();
            link.put("text", linkElement.text());
            link.put("url", linkElement.absUrl("href"));
            links.add(link);
        }
        return links;
    }

    private String extractTextFromSoup(Document soup) {
        List<String> chunks = new ArrayList<String>();
        for (Element element : soup.select("*")) {
            if (!element.tagName().equals("script") && !element.tagName().equals("style")) {
                chunks.add(element.text());
            }
        }
        return String.join("\n", chunks);
    }

    @Override
    public String getDesc() {
        return "Scrape answers for a question in a given web page";
    }

    @Override
    public Map<String, String> getArgs() {
        Map<String, String> args = new HashMap<String, String>();
        args.put("url", "URL of the website to scrape");
        args.put("question", "The question");
        return args;
    }

    @Override
    public Map<String, Object> run(Map<String, String> kwargs) {
        return null;
    }

    @Override
    public Map<String, String> getResp() {
        Map<String, String> resp = new HashMap<String, String>();
        resp.put("text", "Summary of relevant text scraped from the website");
        resp.put("links", "list of links from the website, where each item is in the form `[link_text,link_url]`");
        return resp;
    }

    public void close() {
        try {
            if (this.driver != null) {
                this.driver.quit();
            }
        } catch (Exception e) {
            // Handle exception
        }
    }

    public Map<String, Object> run(String url, String question) {
        Map<String, Object> result = new HashMap<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            // Handle exception
        }
        if (doc != null) {
            doc.select("script, style").remove();
            Elements links = doc.select("a[href]");
            List<Map<String, String>> extractedLinks = new ArrayList<>();
            for (Element link : links) {
                Map<String, String> linkMap = new HashMap<>();
                linkMap.put("text", link.text());
                linkMap.put("href", link.attr("href"));
                extractedLinks.add(linkMap);
                if (extractedLinks.size() >= 5) {
                    break;
                }
            }
            String text = doc.text();
            String[] lines = text.split("\\r?\\n");
            List<String> chunks = new ArrayList<>();
            for (String line : lines) {
                String[] phrases = line.split("\\s{2,}");
                for (String phrase : phrases) {
                    chunks.add(phrase.trim());
                }
            }
            String extractedText = String.join("\n", chunks);
            String summary = this.summarizer.summarize(extractedText, question);
            result.put("text", summary);
            result.put("links", extractedLinks);
        }
        return result;
    }

}
