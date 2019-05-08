package com.squarefactory;

class Category {
    byte large;
    byte medium;
    byte small;
    String name;

    Category(byte large, byte medium, byte small, String name) {
        this.name = name;
        this.large = large;
        this.medium = medium;
        this.small = small;
    }
}
