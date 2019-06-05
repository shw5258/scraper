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

public class Manager {
    public static void main(String[] args) {
        try {
            String query = "select prod_id, price, name from product where " +
                    "name is not null and " +
                    "soldout is false and " +
                    "variants is false and " +
                    "another_form is false and " +
                    "present is true and " +
                    "must_call is false";
            Connection con = MyDataSourceFactory.getMySQLDataSource().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                control(rs, con);
//                System.out.print("|");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void control(ResultSet rs, Connection con) {
        int productId = 0;
        try {
            productId = rs.getInt("prod_id");
            Document doc = Jsoup.connect(DetailCollector.BASE + "/p/" + productId).get();
            Element addToCart = doc.getElementById("addToCartForm");
            if(addToCart != null && addToCart.text().contains("품절")){
                String query = "UPDATE PRODUCT SET SOLDOUT = TRUE WHERE PROD_ID=" + productId;
                con.createStatement().executeUpdate(query);
                System.out.println("sold out: " + productId);
            }else{
                updateAndShow(rs, con, productId, doc);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.out.println("Exception Product(" + productId + ")");
        }
    }

    private static void updateAndShow(ResultSet rs, Connection con, int productId, Document doc) throws SQLException {
        String name = doc.select(".product-name").get(0).text();
        int fromPrice = rs.getInt("price");
        Elements priceEl = doc.select("head > meta[property=product:price:amount]");
        int toPrice = (int) Double.parseDouble(priceEl.attr("content"));
        if (!name.equals(rs.getString("name"))) {
            System.out.println("");
            System.out.println("different product: " + productId);
        }else if (toPrice != rs.getInt("price")) {
            updatePrice(con, productId, toPrice);

            //show the changed product and let manager change the price manually
            fromPrice = (int) Math.round(fromPrice * 1.17 / 100) * 100;
            toPrice = (int) Math.round(toPrice * 1.17 / 100) * 100;
            System.out.println("");
            System.out.println("id: " + rs.getInt("prod_id")
                    + " price: " + fromPrice
                    + " to " + toPrice);
        }
    }

    private static void updatePrice(Connection con, int productId, int toPrice) throws SQLException {
        //if the price is changed, store the price and set change true
        String query = "update product set price = " + toPrice + ", changed = true where prod_id = " + productId;
        con.createStatement().executeUpdate(query);
    }
}
