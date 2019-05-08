package com.squarefactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.logging.Logger;


public class Example {
    private final static Logger logger = Logger.getLogger(Example.class.getName());
    public static void main(String[] args) {
        Document doc;
        try {
            doc = Jsoup.connect("http://en.wikipedia.org/").get();
            logger.info(doc.title());
            Elements newsHeadlines = doc.select("#mp-itn b a");
            for (Element headline : newsHeadlines) {
                String data = String.format("%s\n\t%s",
                        headline.attr("title"), headline.absUrl("href"));
                logger.info(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
