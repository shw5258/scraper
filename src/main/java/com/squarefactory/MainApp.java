package com.squarefactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MainApp {
    public static void main(String[] args) {
        for (String str : args) {
            System.out.println(str);
            if (str.equals("collect")) {
                new CatCollector().collect();
                new ProdNumCollector().collect();
//        new VariantRegister().collect();
                new DetailCollectorCopy().collect();
            } else if (str.equals("manage")) {
                new Manager().manage();
                String message = "품절인 상품번호";
                String select = "select prod_id from product where soldout is true";
                forManualProccess(message, select);
                message = "삭제할 상품번호";
                select = "select prod_id from product where present is false";
                forManualProccess(message, select);
            } else if (str.equals("excel")) {
                new XlsxGenerator().Amethod(100, XlsxGenerator.query4);
            }
        }
    }

    private static void forManualProccess(String message, String select) {
        ResultSet rs = null;
        try {
            rs = MyDataSourceFactory.getMySQLDataSource()
                    .getConnection()
                    .createStatement()
                    .executeQuery(select);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(message);
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                System.out.println(rs.getInt("prod_id"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}