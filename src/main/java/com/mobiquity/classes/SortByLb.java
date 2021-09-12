package com.mobiquity.classes;

import java.util.Comparator;

public class SortByLb implements Comparator<Node> {
    public int compare(Node a, Node b)
    {
        boolean temp = a.getLowerBound() > b.getLowerBound();
        return temp ? 1 : -1;
    }
}