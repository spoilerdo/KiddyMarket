package com.kiddyMarket.Logic;

import com.kiddyMarket.DataInterfaces.IAccountRepository;
import com.kiddyMarket.DataInterfaces.IOfferRepository;
import com.kiddyMarket.Entities.Account;
import com.kiddyMarket.Entities.Offer;
import com.kiddyMarket.Entities.Wrappers.AccountFormWrapper;
import com.kiddyMarket.Entities.Wrappers.AccountWrapper;
import com.kiddyMarket.Entities.Wrappers.BankAccountWrapper;
import com.kiddyMarket.Logic.Helper.RestCallLogic;
import com.kiddyMarket.LogicInterfaces.IAccountLogic;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.kiddyMarket.Logic.Constants.APIConstants.*;

@Service
public class AccountLogic implements IAccountLogic {
    private IAccountRepository accountRepository;
    private IOfferRepository offerRepository;
    private RestCallLogic restCall;

    @Autowired
    public AccountLogic(IAccountRepository accountRepository, IOfferRepository offerRepository, RestCallLogic restCall) {
        this.accountRepository = accountRepository;
        this.offerRepository = offerRepository;
        this.restCall = restCall;
    }

    @Override
    public AccountWrapper CreateBankAccount(AccountFormWrapper accountFormWrapper) {
        //check if required values are filled in
        if(Strings.isNullOrEmpty(accountFormWrapper.getUsername()) || Strings.isNullOrEmpty(accountFormWrapper.getPassword()) || Strings.isNullOrEmpty(accountFormWrapper.getEmail())){
            throw new IllegalArgumentException("Values cannot be null");
        }

        //check if username already exists
        ResponseEntity response = restCall.call(AUTH_ACCOUNTS + accountFormWrapper.getUsername(), null, AccountWrapper.class, HttpMethod.GET, false);
        if(response.getStatusCode() == HttpStatus.OK){
            throw new IllegalArgumentException("Username already in use");
        }

        //create account for Bank-API
        AccountWrapper account = new AccountWrapper(
                accountFormWrapper.getUsername(),
                accountFormWrapper.getPassword(),
                accountFormWrapper.getEmail(),
                accountFormWrapper.getPhoneNumber()
        );

        AccountWrapper bankAccount = restCall.callWithStatusCheck(AUTH_ACCOUNTS, account.toString(), AccountWrapper.class, HttpMethod.POST, false).getBody();

        //create bank-account for Bank-API //TODO: this will be redundant if Tygo fixed the bank API
        BankAccountWrapper bankBankAccount = new BankAccountWrapper(accountFormWrapper.getBankName());
        restCall.callWithStatusCheck(BANKCALL, bankBankAccount.toString(), BankAccountWrapper.class, HttpMethod.POST, false);

        return bankAccount;
    }

    @Override
    public void DeleteBankAccount(int accountId) {
        //check if bank account exists
        restCall.callWithStatusCheck(AUTH_ACCOUNTS + accountId, null, AccountWrapper.class, HttpMethod.GET, true);

        //delete bank-account
        restCall.call(AUTH_ACCOUNTS + accountId, null, int.class, HttpMethod.DELETE, true);

        //delete inventory-account
        restCall.call(INVENTORY_ACCOUNTS + accountId, null, int.class, HttpMethod.DELETE, true);

        //delete market-account
        accountRepository.deleteById(accountId);
    }

    @Override
    public Set<Offer> getOffersFromAccount(int accountId) {
        Account foundAccount = checkAccountExists(accountId);

        return foundAccount.getOffers();
    }

    @Override
    public Iterable<Offer> getNewOffersFromAccount(int accountId) {
        Account foundAccount = checkAccountExists(accountId);

        //get all offer that are new
        return foundAccount.getOffers().stream().filter(Offer::isNews).collect(Collectors.toList());
    }

    @Override
    public void changeOfferNews(int accountId) {
        Account foundAccount = checkAccountExists(accountId);

        //delete all the offers that are sold
        Set<Offer> unSoldOffers = foundAccount.getOffers().stream().filter(offer -> !offer.isSold()).collect(Collectors.toSet());

        //change all the offers New bool to false
        unSoldOffers.forEach(offer -> offer.setNews(false));

        //update the offers list
        foundAccount.setOffers(unSoldOffers);
        accountRepository.save(foundAccount);
    }

    //region Generic exception method

    private Account checkAccountExists(int accountId){
        Optional<Account> foundAccount = accountRepository.findById(accountId);
        if(!foundAccount.isPresent()){
            throw new IllegalArgumentException("Account with id: " + accountId + " not found");
        }

        return foundAccount.get();
    }

    //endregion
}
