package controller.api;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class AemetAPIAccessor {
    private static AemetAPIAccessor aemetAPIController;

    public AemetAPIAccessor() {}

    public static AemetAPIAccessor getInstance() {
        if (aemetAPIController != null) return aemetAPIController;
        return new AemetAPIAccessor();
    }

    public String get(String url, String apiKey) throws IOException {
        return Jsoup.connect(url)
                .timeout(15000)
                .ignoreContentType(true)
                .header("accept", "application/json")
                .header("api_key", apiKey)
                .method(Connection.Method.GET)
                .maxBodySize(0).execute().body();
    }
}
