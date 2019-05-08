package com.squarefactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public class FirstJsoupExample {
    public static void main(String[] args) {
        String[] list = "다시마".split("/");
        System.out.println(list[list.length-1]);
    }
}
