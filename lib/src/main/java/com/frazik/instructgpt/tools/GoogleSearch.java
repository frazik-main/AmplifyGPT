package com.frazik.instructgpt.tools;

import com.google.api.services.customsearch.v1.Customsearch;
import com.google.api.services.customsearch.v1.CustomsearchRequestInitializer;
import com.google.api.services.customsearch.v1.model.Result;
import com.google.api.services.customsearch.v1.model.Search;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class GoogleSearch extends Tool {
    private final String googleApiKey = System.getenv("GOOGLE_API_KEY");
    private final String googleCxId = System.getenv("GOOGLE_CX_ID");

    @Override
    public Map<String, String> getArgs() {
        Map<String, String> map = new HashMap<>();
        map.put("query", "The query to search for");
        return map;
    }

    @Override
    public Map<String, String> getResp() {
        Map<String, String> map = new HashMap<>();
        map.put("results", "A list of results. Each result is of the form [title, link, description]");
        return map;
    }

    private List<List<String>> googleSearch(String query, int numResults) {
        try {
            Customsearch customsearch = new Customsearch.Builder(
                    new com.google.api.client.http.javanet.NetHttpTransport(),
                    new com.google.api.client.json.jackson2.JacksonFactory(),
                    null)
                    .setApplicationName("Google Search API")
                    .setGoogleClientRequestInitializer(new CustomsearchRequestInitializer(googleApiKey))
                    .build();

            Customsearch.Cse.List list = customsearch.cse().list().setQ(query).setCx(googleCxId).setNum(numResults).setFields("items(title, link, snippet)");
            Search results = list.execute();

            List<List<String>> resultList = new ArrayList<>();
            for (Result result : results.getItems()) {
                String snippet = result.getSnippet();
                List<String> innerList = new ArrayList<>();
                innerList.add(result.getTitle());
                innerList.add(result.getLink());
                innerList.add(snippet == null ? "" : snippet);
                resultList.add(innerList);
            }
            return resultList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> run(Map<String, String> args) {
        String query = args.get("query");
        int numResults = args.get("numResults") == null ? 8 : Integer.parseInt(args.get("numResults"));
        Map<String, Object> result = new HashMap<>();
        if (googleApiKey != null && !googleApiKey.trim().isEmpty() && !googleApiKey.equals("your-google-api-key")) {
            result.put("results", googleSearch(query, numResults));
        } else {
            result.put("results", duckduckgoSearch(query, numResults));
        }
        return result;
    }

    private final static String DUCKDUCKGO_SEARCH_URL = "https://duckduckgo.com/html/?q=";
    public static List<List<String>> duckduckgoSearch(String query, int numResults) {
        try {
            Document doc = Jsoup.connect(DUCKDUCKGO_SEARCH_URL + query).get();
            Elements results = Objects.requireNonNull(doc.getElementById("links")).
                    getElementsByClass("results_links");
            List<List<String>> resultList = new ArrayList<>();
            for (int i = 0; i < numResults && i < results.size(); i++) {
                Element result = results.get(i);
                Element title = Objects.requireNonNull(result.getElementsByClass("links_main").first()).
                        getElementsByTag("a").first();
                assert title != null;
                System.out.println("\nURL:" + title.attr("href"));
                System.out.println("Title:" + title.text());
                String snippet = Objects.requireNonNull(result.getElementsByClass("result__snippet").first()).
                        text();
                List<String> innerList = new ArrayList<>();
                innerList.add(title.text());
                innerList.add(title.attr("href"));
                innerList.add(snippet);
                resultList.add(innerList);
            }
            return resultList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

