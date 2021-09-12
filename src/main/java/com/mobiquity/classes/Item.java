package com.mobiquity.classes;

import com.mobiquity.exception.APIException;

public class Item {
        private Integer index;
        private double weight;
        private double cost;

    public Item(Integer index, double weight, double cost) {
        this.index = index;
        this.weight = weight;
        this.cost = cost;
    }

    public boolean isValid() throws APIException {
        if(weight > Constants.MAX_ITEM_WEIGHT) {
            throw new APIException("Max weight is"+ Constants.MAX_ITEM_WEIGHT + "!");
        }

        if(cost > Constants.MAX_ITEM_COST) {
            throw new APIException("Max cost is"+ Constants.MAX_ITEM_COST + "!");
        }
        return true;
    }

    public double getWeight() {
        return weight;
    }

    public double getCost() {
        return cost;
    }

    public Integer getIndex() {
        return index;
    }
}
