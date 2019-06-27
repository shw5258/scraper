package com.squarefactory;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class DetailCollectorCopy {
    protected final static String BASE = "https://www.costco.co.kr";

    public static void main(String[] args) {
        new DetailCollectorCopy().collect();
    }

    public void collect() {
        String resetSoldout = "update product set soldout = false, changed = false, present = true";
        String query = "select prod_id from product where " +
                "another_form is false and must_call is false and present is true and main_im is null";
//                "variants is true and (main_im is null or comple_im is null) and another_form is false";
//                "variants is true and prod_id = 622020";
//                "variants = true and another_form = false and radio_key is null";
//        query = "select prod_id from product where prod_id in (622236,623066)";
//        String query = "select prod_id from product where main_im is null";
//        query = "select prod_id from product where prod_id = 620159";


//                + "limit " + howMany;
//        System.out.println(query);
        ResultSet rs;
        Connection con;
        System.out.println(query);
        try {
            con = MyDataSourceFactory.getMySQLDataSource().getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(resetSoldout);
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                control(rs, con);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void control(ResultSet rs, Connection con){
        int productId = 0;
        String query;
        try {
            productId = rs.getInt("prod_id");
            System.out.print(productId + " ");
            Document doc = Jsoup.connect(BASE + "/p/" + productId).get();

            Element addToCart = doc.getElementById("addToCartForm");
            if (addToCart == null) {
                query = "UPDATE PRODUCT SET MUST_CALL = TRUE WHERE PROD_ID=" + productId;
                con.createStatement().executeUpdate(query);
                System.out.println("must call");
            }else if(addToCart.text().contains("품절")){
                query = "UPDATE PRODUCT SET SOLDOUT = TRUE WHERE PROD_ID=" + productId;
                con.createStatement().executeUpdate(query);
                System.out.println("sold out");

            }else if(doc.select(".variant-section").size() != 0){
                query = "UPDATE PRODUCT SET VARIANTS = TRUE WHERE PROD_ID=" + productId;
                con.createStatement().executeUpdate(query);
                System.out.println("variant");
//                parsingAndStoringForVariant(doc, con, productId);
            }else{
                parsingAndStoring(doc, con, productId, "","");
            }
        } catch (SQLException | IOException | AnotherFormException | NullPointerException e) {
            if (e.getClass() == HttpStatusException.class || e.getClass() == UnknownHostException.class) {
                try {
                    con = MyDataSourceFactory.getMySQLDataSource().getConnection();
                    query = "UPDATE PRODUCT SET PRESENT = FALSE WHERE PROD_ID=" + productId;
                    con.createStatement().executeUpdate(query);
                    System.out.println("not exist");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }else if(e.getClass() == AnotherFormException.class){
                try {
                    con = MyDataSourceFactory.getMySQLDataSource().getConnection();
                    query = "UPDATE PRODUCT SET ANOTHER_FORM = TRUE WHERE PROD_ID=" + productId;
                    con.createStatement().executeUpdate(query);
                    System.out.println("another");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }else{
                e.printStackTrace();
            }
        }
    }

    private void parsingAndStoringForVariant(Document doc, Connection con, int productId)
            throws SQLException, IOException, AnotherFormException{
        Elements radioEls = doc.select("option[value]");
        Elements variantEls = doc.select(".colorVariant");
        ArrayList<String> colorArray = new ArrayList<>();
        ArrayList<String> radioArray = new ArrayList<>();

        if (radioEls.size() != 0) {
            for (Element radioEl : radioEls) {
                radioArray.add(radioEl.text().trim());
            }

        }else{
            for (int i = 0; i < variantEls.size(); i++) {
                String label = variantEls.get(i).attr("aria-label");
                String colorName;
                if (label.contains("-")) {
                    colorName = label.split("-")[1];
                }else{
                    colorName = label;
                }
                if (i == 0) {
                    String relativePath = variantEls.get(i).attr("href");
                    Document deepDoc = Jsoup.connect(BASE + relativePath).get();
                    radioEls = deepDoc.select("option[value]");

                    if (radioEls.size() != 0) {
                        for (Element radioEl : radioEls) {
//                            radioArray.add(radioEl.text().trim());
//                            radioArray.add(radioEl.text().substring(3).trim());
                            radioArray.add(radioEl.text().substring(colorName.length() + 2).trim());
//                            radioArray.add(radioEl.text().substring("스카이".length() + 2).trim());
                        }

                    }
                }
                colorArray.add(colorName);
            }

        }
//        String relativePath = variantEls.get(0).attr("href");
        String colorJoin = String.join(",", colorArray);
        String radioJoin = String.join(",", radioArray);
//        System.out.println(colorJoin + "/" + radioJoin + ":" + BASE + relativePath);
        //colorJoin과 radioJoin을 미리 파싱하여 아래의 일반용 메서드에서 일반상품에 대하여 매번 점검할 필요가 없게 하자
        String radioKey = "";
        String radioVal = "";
        if (!colorJoin.isEmpty() && !radioJoin.isEmpty()) {
            radioKey = "색상\r\n종류";
            radioVal = colorJoin + "\r\n" + radioJoin;
        } else if (!colorJoin.isEmpty()) {
            radioKey = "색상";
            radioVal = colorJoin;
        } else if (!radioJoin.isEmpty()) {
            radioKey = "선택";
            radioVal = radioJoin;
        }
        System.out.println("========================");
        System.out.println(radioKey);
        System.out.println(radioVal);
        System.out.println("========================");
        parsingAndStoring(doc, con, productId, radioKey, radioVal);
    }

    private void parsingAndStoring(Document doc, Connection connection, int productId, String radioKey, String radioVal)
            throws SQLException, IOException, AnotherFormException {
        Elements nameEl = doc.select(".product-name");
        Elements imagEl = doc.select(".lazyOwl");
        Elements wrapEl = doc.select(".wrapper_itemDes");
        Elements iframes = doc.select("iframe");

        for (Element iframe : iframes) {
            iframe.remove();
        }
        if (wrapEl.size() == 0) {
            throw new AnotherFormException();
        }

        Elements itemDes = wrapEl.get(0).select(".txt_itemDes");
        Elements wrapChild = wrapEl.get(0)
                .select(".wrapper_itemDes > p, " +
                        ".wrapper_itemDes > div, " +
                        ".txt_itemDes > ul");

        Elements images = wrapEl.get(0).select("img");
        for (Element image : images) {
            String relative = image.attr("src");
            String absolute = BASE + relative;
            image.attr("src", absolute);
        }

        for (Element tesEl : itemDes) {
            if (tesEl.select("li").html().isEmpty()) {
                tesEl.remove();
            }else{
                tesEl.before("<br>");
            }
        }

        for (Element tes2El : wrapChild) {
            if (tes2El.html().isEmpty()) {
                tes2El.remove();
            }
        }

        wrapEl = doc.select(".wrapper_itemDes");
        Elements desUl = wrapEl.get(0).select("ul");
        for (Element ul : desUl) {
            ul.attr("style", "list-style-type:none");
        }

        Elements specEl = doc.select(".product-classifications > .table > tbody > tr");
        Elements priceEl = doc.select("head > meta[property=product:price:amount]");
        int price = (int) Double.parseDouble(priceEl.attr("content"));
        String name = nameEl.get(0).text();
        String paragraph = wrapEl.html();
        String spec = specEl.html();


        ArrayList<String> urls = new ArrayList<>();
        for (Element im : imagEl) {
            String url = im.attr("data-zoom-image");
            if(!(url == null||url.isEmpty())&&url.contains(".jpg")){
                urls.add(url);
            }
        }
        Element ulEliment = new Element("ul")
                .attr("style", "list-style-type:none;");
        Element parentDiv = new Element("div")
                .append("<br>")
                .append("<h2>상품정보</h2>")
                .appendChild(ulEliment);

        for (int j = 0 ; j < specEl.size() - 1 ; j++) {
            Element oneSpec = specEl.get(j);
            final Element liElement = new Element("li")
                    .attr("margin-top", "1em");
            List<Node> nodes = oneSpec.childNodes();
            for (int i = 0; i < nodes.size(); i++){
                if(i == 1){
                    List<Node> strong = nodes.get(i).childNodesCopy();
                    final Element strongElement = new Element("strong");
                    for (Node n : strong) {
                        strongElement.appendChild(n);
                    }
                    liElement.appendChild(strongElement);
                }else if (i == 3){
                    List<Node> div = nodes.get(i).childNodesCopy();
                    final Element divElement = new Element("div");
                    for (Node n : div) {
                        divElement.appendChild(n);
                    }
                    liElement.appendChild(divElement);
                }
            }
            ulEliment.appendChild(new Element("br"));
            ulEliment.appendChild(liElement);
        }

        //get costco category name for backup plan
        Elements els = doc.select(".breadcrumb a");
        LinkedList<String> coscat = new LinkedList<>();
        for (Element el : els) {
            coscat.add(el.text());
        }
        String deepestCat = coscat.getLast();
        String cosName;
        if (deepestCat.contains("/")) {
            String[] refined = deepestCat.split("/");
            cosName = refined[refined.length - 1];
        }else{
            cosName = deepestCat;
        }


        int naverCategory = 0;
        //Naver doesn't like 'x' character. It won't show anything
        String refinedName = name.replace("x", "");

        //if the product name is not found on Naver then search with the Costco's closest Category name
        if ((naverCategory = getNaverCategory(refinedName)) == 0) {
            naverCategory = getNaverCategory(cosName);
        }
        String query = "update product set cat_naver = " + naverCategory + " where prod_id = " + productId;
        connection.createStatement().executeUpdate(query);

        if(!(name.isEmpty() || price == 0 || naverCategory == 0 || productId == 0)) {
            String detail = wrapEl.toString() + parentDiv.toString();
            detail = detail.replace("'", "\\'");

            //Let's Insert!
            query = "UPDATE PRODUCT " +
                    "SET NAME = '" + name + "'," +
                    "PRICE = " + price + "," +
                    "CAT_NAVER = " + naverCategory + "," +
                    "DETAIL = '" + detail + "'," +
                    "RADIO_KEY = '" + radioKey + "'," +
                    "RADIO_VAL = '" + radioVal + "'," +
                    "CHANGED = TRUE " +
                    "WHERE PROD_ID =" + productId;
            connection.createStatement().executeUpdate(query);


        }else{
            throw new IOException("scraped data is not sufficient");
        }
        ArrayList<String> fileNames = storeImage(urls, productId);
        String mainImage = fileNames.remove(0);
        String complementaryIamges = String.join(",", fileNames);
        String imQuery = "UPDATE PRODUCT " +
                "SET MAIN_IM = '" + mainImage + "', " +
                "COMPLE_IM = '" + complementaryIamges + "' " +
                "WHERE PROD_ID =" + productId;
        connection.createStatement().executeUpdate(imQuery);

        System.out.println("Success");
    }

    private int getNaverCategory(String name) throws IOException, NullPointerException {
        int naverCategory = 0;
        String parsedUrlSnippet = name.replace(" ", "+");
        Document naverDoc = Jsoup.connect("https://search.shopping.naver.com/search/all.nhn?query=" +
                parsedUrlSnippet +
                "&cat_id=&frm=NVSHATC").get();
        System.out.print(name + " ");
        if (naverDoc.select("._itemSection").first() == null) {
            return 0;
        }
        Elements naverCategorys = naverDoc.select("._itemSection").first()
                .select(".info .depth > a");
        if (naverCategorys.size() != 0) {
            TreeSet<String> sorted = new TreeSet<>();
            for (Element anchor : naverCategorys) {
                sorted.addAll(anchor.classNames());
            }
            naverCategory = Integer.parseInt(sorted.last().split("_id_")[1]);
        }
        return naverCategory;
    }

    private ArrayList<String> storeImage(ArrayList<String> imageUrls, int productId){
        final String imageStoragePath = "C:/Users/light/Pictures/CostcoImage/";
        ArrayList<String> fileNames = new ArrayList<>();
        for (int i = 0 ; i < imageUrls.size()&&i<10 ; i++) {
            String imageUrl = imageUrls.get(i);
            String extension =imageUrl.substring(imageUrl.lastIndexOf(".") + 1);
            String fileName = productId + "-" + i + "." + extension;
            fileNames.add(fileName);
            try {
                URL url = new URL(BASE + imageUrl);
                BufferedImage image = ImageIO.read(url);
                String imageFullPath = imageStoragePath + fileName;
                File imfl = new File(imageFullPath);
                ImageIO.write(image, extension, imfl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileNames;
    }
}
