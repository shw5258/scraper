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
import java.util.LinkedHashSet;

public class ProdNumCollector implements BatchWorker.Writer<Product>{

    void collect() {
        Statement stmt;
        String query = "select cat_large, cat_medium, cat_small from category";

        try {
            Connection con = MyDataSourceFactory.getMySQLDataSource().getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            ArrayList<Product> productIds = new ArrayList<>();

            long startTime = System.nanoTime();
            int round = 0;
            while (rs.next()){
                byte large = rs.getByte("cat_large");
                byte medium = rs.getByte("cat_medium");
                byte small = rs.getByte("cat_small");
                String catNum = large + "." + medium + "." + small;
                Document doc = Jsoup.connect("https://www.costco.co.kr/c/cos_" + catNum).get();
                String cssQuery = "a[href~=/p/\\d*$]";
                Elements base = doc.select(cssQuery);
                ArrayList<Integer> duplicatable = new ArrayList<>();
                for (Element aTag:base
                ) {
                    String link = aTag.attr("href");
                    String productToken = link.split("/p/")[1];
                    int productId = Integer.parseInt(productToken);
                    duplicatable.add(productId);
                }
                LinkedHashSet<Integer> unique = new LinkedHashSet<>( duplicatable );
                for (Integer id:unique
                     ) {
                    Category category = new Category(large, medium, small, null);
                    productIds.add(new Product(category, id));
                }
                System.out.println(++round);
            }
//            System.out.println(dataInput);
            new BatchWorker().batchUpdate(this, productIds);

            long endTime   = System.nanoTime();
            long totalTime = endTime - startTime;
            System.out.println(totalTime);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void writeSql(ArrayList<Product> batchData, Statement stmt) throws SQLException {
        for (Product product: batchData) {
            stmt.addBatch("INSERT IGNORE INTO PRODUCT (PROD_ID, CAT_LARGE, CAT_MEDIUM, CAT_SMALL) VALUES (" +
//            System.out.println("INSERT INTO PRODUCT (PROD_ID, CAT_LARGE, CAT_MEDIUM, CAT_SMALL) VALUES (" +
                    product.id + "," +
                    product.category.large + "," +
                    product.category.medium + "," +
                    product.category.small + ")"
            );
        }
    }
}
