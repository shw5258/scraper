package com.squarefactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class VariantRegister implements BatchWorker.Writer<Variant> {

    public static void main(String[] args) {
        new VariantRegister().collect();
    }

    void collect() {
        Statement stmt;
        String query = "select prod_id from product where variants is true";

        try {
            Connection con = MyDataSourceFactory.getMySQLDataSource().getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            ArrayList<Variant> productIds = new ArrayList<>();

            long startTime = System.currentTimeMillis();
            while (rs.next()){
                int prodNum = rs.getInt("prod_id");
                Document doc = Jsoup.connect("https://www.costco.co.kr/p/" + prodNum).get();
                Elements baseSquare = doc.select(".variant-list > li > a");
                Elements baseRadio = doc.select("#variant_select-999 > option[value]");
                HashMap<String,Boolean> colorHash = new HashMap<>();
                HashMap<String,Boolean> radioHash = new HashMap<>();
                if (baseSquare.size() != 0) {

                    for (Element aTag:baseSquare) {
                        String link = aTag.attr("href");
                        String productToken = link.split("/p/")[1];
                        String colorId = productToken;
                        Document colorDoc = Jsoup.connect("https://www.costco.co.kr/p/" + productToken).get();
                        Elements deepRadio = colorDoc.select("#variant_select-999 > option[value]");
                        if (deepRadio.size() == 0) {

                            Element addToCart = colorDoc.getElementById("addToCartForm");
                            if (addToCart != null && addToCart.text().contains("품절")) {
                                colorHash.put(colorId, true);
                            } else {
                                colorHash.put(colorId, false);
                            }
                        } else {
                            for (Element optionTag : deepRadio) {
                                String deepLink = optionTag.attr("value");
                                String deepProdToken = deepLink.split("/p/")[1];
                                String deepSelectId = deepProdToken;
                                Document radioDoc = Jsoup.connect("https://www.costco.co.kr/p/" + deepProdToken).get();
                                Element addToCart = radioDoc.getElementById("addToCartForm");
                                if(addToCart != null && addToCart.text().contains("품절")){
                                    colorHash.put(deepSelectId,true);
                                }else{
                                    colorHash.put(deepSelectId,false);
                                }
                            }
                        }
                    }
                } else if (baseRadio.size() != 0) {

                    for (Element optionTag : baseRadio) {
                        String link = optionTag.attr("value");
                        String productToken = link.split("/p/")[1];
                        String selectId = productToken;
                        Document radioDoc = Jsoup.connect("https://www.costco.co.kr/p/" + productToken).get();
                        Element addToCart = radioDoc.getElementById("addToCartForm");
                        if(addToCart != null && addToCart.text().contains("품절")){
                            colorHash.put(selectId,true);
                        }else{
                            colorHash.put(selectId,false);
                        }
                    }
                }
                System.out.println(prodNum + " " + colorHash + radioHash);
//                for (String id:unique
//                ) {
//                    Category category = new Category(large, medium, small, null);
//                    productIds.add(new Variant(category, id));
//                }
            }
            new BatchWorker().batchUpdate(this, productIds);

            long endTime   = System.currentTimeMillis();
            long milliseconds = endTime - startTime;
            long minutes = (milliseconds / 1000) / 60;
            long seconds = (milliseconds / 1000) % 60;
            System.out.format("%d minutes and %d seconds.", minutes, seconds);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void writeSql(ArrayList<Variant> batchData, Statement stmt) throws SQLException {
        for (Variant product: batchData) {
            stmt.addBatch("INSERT IGNORE INTO VARIANT (PROD_ID, CAT_LARGE, CAT_MEDIUM, CAT_SMALL, SOLDOUT) VALUES ('" +
//            System.out.println("INSERT INTO VARIANT (PROD_ID, CAT_LARGE, CAT_MEDIUM, CAT_SMALL) VALUES ('" +
                            product.id + "'," +
                            product.category.large + "," +
                            product.category.medium + "," +
                            product.category.small + "," +
                            product.soldout + ")"
            );
        }
    }
}