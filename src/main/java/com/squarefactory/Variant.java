package com.squarefactory;

public class Variant {
    Category category;
    String id;
    boolean soldout;

    Variant(Category category, String id, boolean soldout){
        this.category = category;
        this.id = id;
        this.soldout = soldout;
    }
}
