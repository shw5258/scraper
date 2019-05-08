package com.squarefactory;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class XlsxGenerator {
    private List<NaverProduct> xlsxData = new ArrayList<NaverProduct>();
    private List<String> productIds = new ArrayList<String>();

    public static void main(String[] args) {
        new XlsxGenerator().Amethod();
    }

    public void Amethod(){
        String query = "select prod_id, name, price, detail, cat_naver, main_im, comple_im " +
                "from product " +
                "where main_im is not null";
        try {
            Connection connection = MyDataSourceFactory.getMySQLDataSource().getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int runningSum = 0;
            int fileTurn = 1;
            while (rs.next()) {

                String compleImages = rs.getString("comple_im");
                int count = compleImages.split("jpg", -1).length - 1;
                if (runningSum + count > 500) {
                    //1.make xlsx file before the new begin
                    makeXlsx(fileTurn++);
                    //initialize ArrayList
                    xlsxData = new ArrayList<NaverProduct>();
                    productIds = new ArrayList<String>();
                    //add this a row
                    populateRow(rs);
                    runningSum = 0;
                    runningSum += count;
                }else{
                    //add this a row
                    populateRow(rs);
                    runningSum += count;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateRow(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        int price = rs.getInt("price");
        int naverCategory = rs.getInt("cat_naver");
        String mainImage = rs.getString("main_im");
        String complementaryImage = rs.getString("comple_im");
        String detail = rs.getString("detail");
        int sellerProductCode = rs.getInt("prod_id");
        xlsxData.add(new NaverProduct(naverCategory, name, price, mainImage,
                complementaryImage, detail, sellerProductCode));
        productIds.add("" + sellerProductCode);
    }


    public void makeXlsx(int turn) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Naver Products");

        // Create a Row
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < 66; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(i);
        }

        // Create Other rows and cells with contacts data
        int rowNum = 1;

        for (NaverProduct product : xlsxData) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.brandnew);
            row.createCell(1).setCellValue(product.naverCategory);
            row.createCell(2).setCellValue(product.name);
            row.createCell(3).setCellValue(product.price);
            row.createCell(4).setCellValue(product.inStock);
            row.createCell(5).setCellValue(product.afterService);
            row.createCell(6).setCellValue(product.contact);
            row.createCell(7).setCellValue(product.mainImage);
            row.createCell(8).setCellValue(product.complementaryImage);
            row.createCell(9).setCellValue(product.detail);
            row.createCell(10).setCellValue(product.sellerProductCode);
            row.createCell(16).setCellValue(product.vat);
            row.createCell(17).setCellValue(product.underAge);
            row.createCell(18).setCellValue(product.commentary);
            row.createCell(19).setCellValue(product.originCode);
            row.createCell(21).setCellValue(product.multiOrigin);
            row.createCell(23).setCellValue(product.deliveryType);
            row.createCell(24).setCellValue(product.deliveryCondition);
            row.createCell(25).setCellValue(product.basicDeliveryFee);
            row.createCell(26).setCellValue(product.deliveryPay);
            row.createCell(29).setCellValue(product.returnFee);
            row.createCell(30).setCellValue(product.changeFee);
            row.createCell(32).setCellValue(product.setupFee);
            row.createCell(62).setCellValue(product.storeZzim);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("C:\\Users\\shw52\\OneDrive\\문서\\costco project\\output\\Products" + turn + ".xlsx");
        workbook.write(fileOut);
        fileOut.close();

        String searchStr = String.join("OR", productIds);
        try (PrintWriter out = new PrintWriter("C:\\Users\\shw52\\OneDrive\\문서\\costco project\\output\\search" + turn + ".txt")) {
            out.println(searchStr);
        }
    }
}
