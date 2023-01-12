package web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class HTMLMaker {
    public static HTMLMaker htmlMaker;

    public static HTMLMaker getInstance() {
        if (htmlMaker == null)
            htmlMaker = new HTMLMaker();
        return htmlMaker;
    }

    public HTMLMaker() {}

    public String feedHTML(String json, String url) {
        File file = new File(url);
        StringBuilder html = new StringBuilder();

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);

            String line = br.readLine();
            while (line != null) {
                if (line.charAt(0) == '<' && line.charAt(1) == '!') {
                    html.append("<p>" + json + "</p>");
                } else {
                    html.append(line);
                }

                line = br.readLine();
            }
        } catch (Exception ignored) {}

        return html.toString();
    }
}
