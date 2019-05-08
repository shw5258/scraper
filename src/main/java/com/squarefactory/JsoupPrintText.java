//package com.squarefactory;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//
//import java.io.IOException;
//
//public class JsoupPrintText {
//    public static void main(String[] args) {
//        try {
//
//            Document doc = Jsoup.connect("https://www.costco.co.kr/p/620778").get();
//            Elements nameEl = doc.select("#globalMessages > div.product-page-container > div.product-title-container.hidden-md.hidden-lg.visible-tablet-landscape.col-xs-12.top-title > h1");
//            Elements paragraphEl = doc.select("#collapse-PRODUCT_DETAILS > div > div > div.product-details-content-wrapper > div");
////            Elements specification = doc.select("#collapse-PRODUCT_DETAILS > div > div > div.product-details-content-wrapper > div > div > div:nth-child(1)");
//            Elements priceEl = doc.select("head > meta[property=product:price:amount]");
//            int price = (int) Double.parseDouble(priceEl.attr("content"));
//            String name = nameEl.text();
//            String paragraph = paragraphEl.text();
//
//            System.out.println(name);
//            System.out.println(paragraph);
////            System.out.println(specification.html());
//            System.out.println(price);
//
//
//            MysqlCon table = new MysqlCon();
//            table.insert(name, paragraph, price);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
