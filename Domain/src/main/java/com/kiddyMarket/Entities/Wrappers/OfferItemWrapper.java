package com.kiddyMarket.Entities.Wrappers;

import com.kiddyMarket.Entities.Offer;

public class OfferItemWrapper {
    //this entity has all the information for a website to fill a list
    //you need some item information in order to know the name of the skin you want to sell
    private Offer offer;
    private ItemWrapper item;

    public OfferItemWrapper() {
    }

    public OfferItemWrapper(Offer offer, ItemWrapper item) {
        this.offer = offer;
        this.item = item;
    }

    public Offer getOffer() {
        return offer;
    }

    public ItemWrapper getItem() {
        return item;
    }
}
