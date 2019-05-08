package com.squarefactory;

public class NaverProduct {
    String name;
    int price;
    int naverCategory;
    String mainImage;
    String complementaryImage;
    String detail;
    int sellerProductCode;
    String brandnew = "신상품";
    int inStock = 999;
    String afterService = "010-9170-5799";
    String contact = "010-9170-5799";
    String vat = "과세상품";
    String underAge = "Y";
    String commentary = "Y";
    String originCode = "03";
    String multiOrigin = "N";
    String deliveryType = "택배‚ 소포‚ 등기";
    String deliveryCondition = "유료";
    int basicDeliveryFee = 3000;
    String deliveryPay = "선결제";
    int returnFee = 5000;
    int changeFee = 10000;
    String setupFee = "N";
    String storeZzim = "N";

    public NaverProduct(int naverCategory, String name, int price, String mainImage,
                        String complementaryImage, String detail, int sellerProductCode) {
        this.naverCategory = naverCategory;
        this.name = name;
        this.price = price;
        this.mainImage = mainImage;
        this.complementaryImage = complementaryImage;
        this. detail = detail;
        this.sellerProductCode = sellerProductCode;
    }

}
