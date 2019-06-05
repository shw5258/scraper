package com.squarefactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class CopyToFolder {
    public static void main(String[] args) {
        int places = 2;
        double value = 32475.356;
        double result = Math.round(value / 100) * 100;
        System.out.println((int)result);
    }
}