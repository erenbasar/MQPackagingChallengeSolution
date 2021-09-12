package com.mobiquity.classes;

import java.util.List;

public class Package {

    private List<Item> items;
    private int capacity;

    public Package(List<Item> items, int capacity) {
        this.items = items;
        this.capacity = capacity;
    }

    public List<Item> getItems() {
        return items;
    }

}
