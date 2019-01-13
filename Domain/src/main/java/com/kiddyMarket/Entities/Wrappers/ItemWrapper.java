package com.kiddyMarket.Entities.Wrappers;

import com.kiddyMarket.Entities.Enums.Condition;
import com.kiddyMarket.Entities.Enums.Quality;

public class ItemWrapper {
    //this entity has all the information in order to get the item from the Inventory API
    private String name;
    private String description;
    private Condition condition;
    private Quality quality;
    private Float price;

    public ItemWrapper() {
    }

    public ItemWrapper(String name, String description, Condition condition, Quality quality, Float price) {
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.quality = quality;
        this.price = price;
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

    public Quality getQuality() {
        return quality;
    }

    public Float getPrice() {
        return price;
    }
}
