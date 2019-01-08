package com.kiddyMarket.LogicInterfaces;

import com.kiddyMarket.Entities.Offer;
import com.kiddyMarket.Entities.Wrappers.OfferItemWrapper;
import org.springframework.security.core.Authentication;

import java.security.Principal;

public interface IOfferLogic {
    /**
     * gets the offer from the specified offerId
     * @param offerId the id of the offer you want to get
     * @return the founded offer
     * @throws IllegalArgumentException if the offer cant be found in the system
     */
    OfferItemWrapper getOfferDetails(int offerId);
    /**
     * create a offer with give parameter
     * @param user the claim of the logged in user
     * @param offer the offer you want to create
     * @return the created offer
     * @throws IllegalArgumentException if offer values are empty
     * @throws IllegalArgumentException if the user has given a non legit bank number
     */
    Offer createOffer(Authentication user, Offer offer);
    /**
     * delete a offer with given parameter
     * @param user the claim of the logged in user
     * @param offerId the id of the offer you want to delete
     * @throws IllegalArgumentException if the given offer is not linked to the given account
     */
    void deleteOffer(Authentication user, int offerId);
    /**
     * edit a offer with given parameter
     * @param user the claim of the logged in user
     * @param offer the offer you want to update
     * @return the updated offer
     * @throws IllegalArgumentException if offer values are empty
     * @throws IllegalArgumentException if the given offer is not linked to the given account
     */
    Offer updateOffer(Authentication user, Offer offer);
    /**
     * accepts the offer to begin the transaction between item and price
     * @param user the claim of the logged in user
     * @param offer the offer you accepted
     * @param bankNumber the number of the bank account the user wants to use
     * @throws IllegalArgumentException if the offer values are empty
     * @throws IllegalArgumentException if the offer is not found in the system
     * @throws IllegalArgumentException if the user has given a non legit bank number
     * @throws IllegalArgumentException if the user hasn't enough balance
     */
    void acceptOffer(Authentication user, Offer offer, int bankNumber);
    /**
     * get all offers
     * @return a list of offers
     */
    Iterable<Offer>getAllOffers();
}