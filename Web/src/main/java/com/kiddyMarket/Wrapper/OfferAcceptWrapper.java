package com.kiddyMarket.Wrapper;

import com.kiddyMarket.Entities.Offer;

public class OfferAcceptWrapper {
    private Offer offer;
    private int bankNumber;

    public OfferAcceptWrapper(){}

    public OfferAcceptWrapper(Offer offer, int bankNumber) {
        this.offer = offer;
        this.bankNumber = bankNumber;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public int getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(int bankNumber) {
        this.bankNumber = bankNumber;
    }
}
