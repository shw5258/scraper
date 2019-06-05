package com.squarefactory;

public class MainApp {
    public static void main(String[] args) {
        new CatCollector().collect();
        new ProdNumCollector().collect();
        new DetailCollector().collect();
    }
}