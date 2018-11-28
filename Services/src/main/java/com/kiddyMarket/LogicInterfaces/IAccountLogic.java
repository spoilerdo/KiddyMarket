package com.kiddyMarket.LogicInterfaces;

import com.kiddyMarket.Entities.Wrappers.AccountFormWrapper;

public interface IAccountLogic {
    /**
     * Create a bank-API account in order to use the kiddy-network
     * @param accountFormWrapper the necessary data to make a bank-account
     * @return nothing if everything goes correct
     * @throws IllegalArgumentException if the required values are not filled in
     * @throws IllegalArgumentException if the given username or email already exists
     */
    void CreateBankAccount(AccountFormWrapper accountFormWrapper);
}
