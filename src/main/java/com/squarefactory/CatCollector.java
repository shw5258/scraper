package com.squarefactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class CatCollector implements BatchWorker.Writer <Category>{
    public void collect() {
//        try {
//            viewTable(MyDataSourceFactory.getMySQLDataSource().getConnection());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        try {
            Document doc = Jsoup.connect("https://www.costco.co.kr").get();
            String cssQuery = "a[href~=/c/cos_\\d*\\.\\d*\\.\\d*$]";
            Elements base = doc.select(cssQuery);
            ArrayList<Category> catCodes = new ArrayList<>();
            for (Element atag: base) {
                String catNum = atag.attr("href").split("/c/cos_")[1];
                String name = atag.text();
                byte large;
                byte medium;
                byte small;
                String[] catStr = catNum.split("\\.");
                large = Byte.valueOf(catStr[0]);
                medium = Byte.valueOf(catStr[1]);
                small = Byte.valueOf(catStr[2]);
                catCodes.add(new Category(large, medium, small, name));
//                ProdNumCollector.collect(catNum);
            }
            new BatchWorker().batchUpdate(this, catCodes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeSql(ArrayList<Category> batchData, Statement stmt) throws SQLException {
        for (Category section: batchData) {
            stmt.addBatch("INSERT INTO CATEGORY VALUES (" +
//                System.out.println("INSERT INTO CATEGORY VALUES (" +
                            section.large + "," +
                            section.medium + "," +
                            section.small + ",'" +
                            section.name + "')"
            );
        }
    }

}
