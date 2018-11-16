package com.kiddyMarket.LogicInterfaces;

import com.kiddyMarket.Entities.Offer;

import java.security.Principal;

public interface IOfferLogic {
    /**
     *create a offer with give parameter
     * @param offer the offer you want to create
     * @returns the created offer
     * @throws IllegalArgumentException if user exists in the system
     * @throws IllegalArgumentException if offer values are empty
     */
    Offer createOffer(Offer offer);
    /**
     * delete a offer with given parameter
     * @param offerId the id of the offer you want to delete
     * @return nothing
     * @throws IllegalArgumentException if offer is not found in the system
     */
    void deleteOffer(int offerId);
    /**
     * edit a offer with given parameter
     * @param offer the offer you want to update
     * @return the updated offer
     * @throws IllegalArgumentException if offer values are empty
     * @throws IllegalArgumentException if offer is not found in the system
     */
    Offer updateOffer(Offer offer);
    /**
     * accepts the offer to begin the transaction between item and price
     * @param offer the offer you accepted
     * @throws IllegalArgumentException if the account is not found in the system
     * @throws IllegalArgumentException if the offer values are empty
     * @throws IllegalArgumentException if the offer is not found in the system
     */
    void acceptOffer(Offer offer);
    /**
     * TODO: dit zijn waarschijnlijk te veel offers in een keer dus miss de eerste 10 etc...
     * get all offers
     * @return a list of offers
     */
    Iterable<Offer>getAllOffers();
}