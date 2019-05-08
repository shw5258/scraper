package com.squarefactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

public class JsoupParser {
    public static void main(String[] args) {
        final String input = "  <table class=\"table\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td class=\"attrib\">식품의 유형</td>\n" +
                "        <td class=\"attrib-val\">과채음료</td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>";
        final Document.OutputSettings settings = new Document.OutputSettings();
        settings.prettyPrint(false);
        final Document document = Jsoup.parseBodyFragment(input);
        document.outputSettings(settings);
//        final Tag tag = Tag.valueOf("strong");
//        final Element td1 = document.getElementsByTag("td").get(0);
//        final Element newElement = new Element(tag, "");
//        newElement.html(td1.html());
//        td1.replaceWith(newElement);
        System.out.println(document);
    }
}
