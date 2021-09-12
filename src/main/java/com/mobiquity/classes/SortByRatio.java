package com.mobiquity.classes;
import java.util.Comparator;

public class SortByRatio implements Comparator<Item> {
    public int compare(Item a, Item b)
    {
        boolean temp = a.getCost()
                / a.getWeight()
                > b.getCost()
                / b.getWeight();
        return temp ? -1 : 1;
    }
}