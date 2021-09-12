package com.mobiquity.classes;

public class Node {
    // Upper Bound: Best case
    private double upperBound;

    // Lower Bound: Worst case
    private double lowerBound;

    // Level of the node
    private int level;

    // item selected or not
    private boolean flag;

    // Total Value
    private double totalValue;

    // Total Weight
    private double totalWeight;

    public Node() {}
    public Node(Node cpy)
    {
        this.totalValue = cpy.totalValue;
        this.totalValue = cpy.totalValue;
        this.totalWeight = cpy.totalWeight;
        this.upperBound = cpy.upperBound;
        this.lowerBound = cpy.lowerBound;
        this.level = cpy.level;
        this.flag = cpy.flag;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }
}
