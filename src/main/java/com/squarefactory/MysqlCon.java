package com.squarefactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class MysqlCon {
    Statement stmt = null;
    ResultSet rs = null;
    Connection con = null;

    public void insert(String name, String detail, int price) {

        name = StringUtils.escapeString(name);
        detail = StringUtils.escapeString(detail);

        String sqlStr = "INSERT INTO Products (NAME, DETAIL, PRICE) VALUES ('" +
                name + "','" + detail + "'," + price +
                ")";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/squarefactory" +
                            "?useSSL=false&serverTimezone=UTC", "root", "368541");
            System.out.println("connetion created");
            stmt = con.createStatement();
            System.out.println("statement opened");
            stmt.executeUpdate(sqlStr);
            System.out.println("result set received");
//            while (rs.next())
//                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                    System.out.println("resultSet close exception");
                } // ignore

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                    System.out.println("statment close exception");
                } // ignore

                stmt = null;
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.out.println("connection close exception");
                }
                con = null;
            }
        }
    }
}  