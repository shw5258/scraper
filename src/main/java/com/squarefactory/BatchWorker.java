package com.squarefactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BatchWorker {
    public void batchUpdate(Writer writer, ArrayList<?> batchData){

        Statement stmt = null;
        Connection con = null;
        try {
            con = MyDataSourceFactory.getMySQLDataSource().getConnection();

            con.setAutoCommit(false);
            stmt = con.createStatement();

            writer.writeSql(batchData, stmt);

            stmt.executeBatch();
            con.commit();

        } catch (SQLException b) {
            b.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null){
                try {
                    con.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface Writer<T> {

        void writeSql(ArrayList<T> batchData, Statement stmt) throws SQLException;
    }

}
