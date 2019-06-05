package com.squarefactory;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XlsxGenerator {
    private List<NaverProduct> xlsxData = new ArrayList<NaverProduct>();
    private List<String> imageArray = new ArrayList<String>();

    //전체자료 혹은 업데이트된 자료중에 하나만 선택한다
    public static void main(String[] args) {
        //상세정보수집이 끝난후 정보가 있는 것들만 걸러냄
        String query1 = "select prod_id, name, price, detail, cat_naver, main_im, comple_im " +
                "from product " +
                "where main_im is not null";

        //새로운 상품만의 그룹으로 걸러냄
        String query2 = "select prod_id, name, price, detail, cat_naver, main_im, comple_im " +
                "from product " +
                "where changed is true and soldout is false and variants is false and must_call is false";

        //전체 사진을 다시 분류하기
//        new XlsxGenerator().Amethod(1, query1);

        //업데이트된 사진만 분류하기 100번대부터 시작 즉, 기존 분류사진과 격리시키겠다는 것
        new XlsxGenerator().Amethod(100, query2);
    }

    public void Amethod(int fileTurn, String query){

        try {
            Connection connection = MyDataSourceFactory.getMySQLDataSource().getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int runningSum = 0;

            while (rs.next()) {

                String compleImages = rs.getString("comple_im");
                int comple = 0;
                if (!compleImages.isEmpty()) {
                    comple = compleImages.split("jpg", -1).length - 1;
                }
                //main image plus complementary images
                int count = 1 + comple;

                if (runningSum + count > 500) {
                    //1.make xlsx file before the new begin
                    makeXlsx(fileTurn);
                    copyImagesToDesignatedDirectory(fileTurn);
                    fileTurn++;
                    //initialize ArrayList
                    xlsxData = new ArrayList<NaverProduct>();
                    imageArray = new ArrayList<String>();
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
            makeXlsx(fileTurn);
            copyImagesToDesignatedDirectory(fileTurn);
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
        imageArray.add(mainImage);
        if (!complementaryImage.isEmpty()) {
            String[] names = complementaryImage.split(",");
            imageArray.addAll(Arrays.asList(names));
        }
    }


    public void makeXlsx(int turn) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Naver Products");

        // Create a Row
        Row headerRow = sheet.createRow(0);

        DataFormat fmt = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(fmt.getFormat("@"));
        for (int i = 0; i < 66; i++) {
            Cell cell = headerRow.createCell(i);
            if (i == 19) {
                cell.setCellStyle(cellStyle);
            }
            cell.setCellValue(i);
        }

        // Create Other rows and cells with contacts data
        int rowNum = 1;

        for (NaverProduct product : xlsxData) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.brandnew);
            row.createCell(1).setCellValue(product.naverCategory);
            row.createCell(2).setCellValue(product.name);
            double result = Math.round(product.price * 1.17 / 100) * 100;
            row.createCell(3).setCellValue((int) result);
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
            Cell special = row.createCell(19);
            special.setCellStyle(cellStyle);
            special.setCellValue(product.originCode);
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
        FileOutputStream fileOut = new FileOutputStream("C:\\Users\\light\\OneDrive\\문서\\costco project\\output\\Products" + turn + ".xlsx");
        workbook.write(fileOut);
        fileOut.close();
    }

    private void copyImagesToDesignatedDirectory(int turn) throws IOException {
        new File("C:\\Users\\light\\Pictures\\UploadImage\\" + turn).mkdir();
        for (String fileName : imageArray) {
            Path copied = Paths.get("C:\\Users\\light\\Pictures\\UploadImage\\"+ turn +"\\" + fileName);
            Path originalPath = Paths.get("C:\\Users\\light\\Pictures\\CostcoImage\\" + fileName);
            Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
        }

    }
}
