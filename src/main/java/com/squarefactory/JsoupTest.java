package com.squarefactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

public class JsoupTest {
    public static void main(final String[] args) {
        final String input = "<span style=\"font-family: Arial;\">TEXT<a>mylink</a></span>";
        final Document.OutputSettings settings = new Document.OutputSettings();
        settings.prettyPrint(false);
        final Document document = Jsoup.parseBodyFragment(input);
        document.outputSettings(settings);
//        final Tag tag = Tag.valueOf("arial");
//        final Element span = document.getElementsByTag("span").get(0);
////        final Element newElement = new Element(tag, "");
////        newElement.html(span.html());
////        span.replaceWith(newElement);
////        System.out.print(document.body().children());
//        Element el = new JsoupTest().changeElementTag(document, span, "daniel");
//        System.out.println(el);
//        System.out.println("=======================");
        System.out.println(document);
    }

    private Element changeElementTag(Document document, Element e, String newTag) {
        Element newElement = document.createElement(newTag);
        /* JSoup gives us the live child list, so we need to make a copy. */
        List<Node> copyOfChildNodeList = new ArrayList<Node>();
        copyOfChildNodeList.addAll(e.childNodes());
        System.out.println("e.childNodes(): " + e.childNodes());
        System.out.println("===================");
        for (Node n : copyOfChildNodeList) {
//            n.remove();
            System.out.println(e);
            System.out.println("========================");
            newElement.appendChild(n);
        }
        e.replaceWith(newElement);
        return newElement;
    }

}
