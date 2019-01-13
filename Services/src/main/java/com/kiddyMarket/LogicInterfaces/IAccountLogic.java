package com.kiddyMarket.LogicInterfaces;

import com.kiddyMarket.Entities.Offer;
import com.kiddyMarket.Entities.Wrappers.AccountFormWrapper;
import com.kiddyMarket.Entities.Wrappers.AccountWrapper;

import java.util.List;
import java.util.Set;

public interface IAccountLogic {
    /**
     * Create a bank-API account in order to use the kiddy-network
     * @param accountFormWrapper the necessary data to make a bank-account
     * @return nothing if everything goes correct
     * @throws IllegalArgumentException if the required values are not filled in
     * @throws IllegalArgumentException if the given username or email already exists
     */
    AccountWrapper createBankAccount(AccountFormWrapper accountFormWrapper);
    /**
     * Delete a bank-API account
     * @param accountId the id of the bank-account that needs to be deleted
     * @return nothing if everything goes correct
     * @throws IllegalArgumentException if the given account id doesn't exist in the system
     */
    void deleteBankAccount(int accountId);
    /**
     * get the amount of buy tokens from a account
     * @param accountId the id of the account you want the tokens from
     * @return a number of tokens
     * @throws IllegalArgumentException if account is not found in the system
     */
    int getBuyTokens(int accountId);
    /**
     * get all the offers from a account
     * @param accountId the id of the account you want to get the offers from
     * @return list of offers
     * @throws IllegalArgumentException if account is not found in the system
     */
    List<Offer> getOffersFromAccount(int accountId);
    /**
     * get all the new offers from a account
     * @param accountId the id of the account you want to get the offers from
     * @return list of offers
     * @throws IllegalArgumentException if account is not found in the system
     */
    Iterable<Offer>getNewOffersFromAccount(int accountId);
    /**
     * set the News bool in a offer to false because the player has seen the update to the offer
     * @param accountId the id of the account you want to get the offers from
     * @throws IllegalArgumentException if account is not found in the system
     */
    void changeOfferNews(int accountId);
}
