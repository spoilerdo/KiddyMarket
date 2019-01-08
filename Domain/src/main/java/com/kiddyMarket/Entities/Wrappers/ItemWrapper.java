package com.kiddyMarket.Entities.Wrappers;

import com.kiddyMarket.Entities.Enums.Condition;

public class ItemWrapper {
    //this entity has all the information in order to get the item from the Inventory API
    private String name;
    private String description;
    private Condition condition;
    private Float price;

    public ItemWrapper() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Condition getCondition() {
        return condition;
    }

    public Float getPrice() {
        return price;
    }
}
