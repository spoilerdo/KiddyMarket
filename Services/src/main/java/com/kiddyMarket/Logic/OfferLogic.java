package com.kiddyMarket.Logic;

import com.kiddyMarket.DataInterfaces.IAccountRepository;
import com.kiddyMarket.DataInterfaces.IOfferRepository;
import com.kiddyMarket.Entities.*;
import com.kiddyMarket.Entities.Wrappers.BankAccountWrapper;
import com.kiddyMarket.Entities.Wrappers.ItemWrapper;
import com.kiddyMarket.Entities.Wrappers.OfferItemWrapper;
import com.kiddyMarket.Logic.Helper.RestCallLogic;
import com.kiddyMarket.LogicInterfaces.IOfferLogic;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.kiddyMarket.Logic.Constants.APIConstants.*;

@Service
public class OfferLogic implements IOfferLogic {
    private IOfferRepository offerRepository;
    private IAccountRepository accountRepository;
    private RestCallLogic restCall;

    @Autowired
    public OfferLogic(IOfferRepository offerRepository, IAccountRepository accountRepository, RestCallLogic restCall){
        this.offerRepository = offerRepository;
        this.accountRepository = accountRepository;
        this.restCall = restCall;
    }

    @Override
    public OfferItemWrapper getOfferDetails(int offerId) {
        Offer foundOffer = checkOfferExists(offerId);

        //get the item of the offer
        ItemWrapper foundItem = restCall.callWithStatusCheck(INVENTORY_ITEM_GET + foundOffer.getItemId(), null, ItemWrapper.class, HttpMethod.GET, false).getBody();

        return new OfferItemWrapper(foundOffer, foundItem);
    }

    @Override
    public Offer createOffer(Authentication user, Offer offer) {
        int userId = (int)user.getPrincipal();

        checkOfferValuesNotEmpty(offer);

        checkBankNumber(userId, offer.getSenderBankNumber());

        Account foundAccount = checkAccountExists(userId);

        foundAccount.getOffers().add(offer);
        offer.getAccounts().add(foundAccount);

        //add a buyToken to the user's account so it can buy another item
        foundAccount.setBuyTokens(foundAccount.getBuyTokens() + 1);

        //save the offer and link it to the user's account
        offerRepository.save(offer);
        accountRepository.save(foundAccount);

        return offer;
    }

    @Override
    public void deleteOffer(Authentication user, int offerId) {
        Offer foundOffer = checkAccountHasOffer((int)user.getPrincipal(), offerId);

        offerRepository.delete(foundOffer);
    }

    @Override
    public Offer updateOffer(Authentication user, Offer offer) {
        checkOfferValuesNotEmpty(offer);

        checkAccountHasOffer((int)user.getPrincipal(), offer.getOfferId());

        return offerRepository.save(offer);
    }

    @Override
    public void acceptOffer(Authentication user, Offer offer, int bankNumber) {
        int userId = (int)user.getPrincipal();

        //check if user has enough buyTokens
        Account foundAccount = checkAccountExists(userId);
        if(foundAccount.getBuyTokens() <= 0){
            throw new IllegalArgumentException("not enough tokens to buy this item");
        }

        checkOfferValuesNotEmpty(offer);

        checkOfferExists(offer.getOfferId());

        BankAccountWrapper foundSenderBankAccount = checkBankNumber(userId, bankNumber);

        //check if the balance is high enough for the price
        if(foundSenderBankAccount.getBalance() < offer.getPrice()){
            throw new IllegalArgumentException("balance is to small to buy this item");
        }

        BankAccountWrapper foundReceiverBankAccount = checkBankNumber(offer.getSenderId(), offer.getSenderBankNumber());

        //transfer the item to the buyer
        ItemTransfer itemTransfer = new ItemTransfer(offer.getSenderId(), userId, offer.getItemId());
        restCall.callWithStatusCheck(TRANSFER_ITEM, itemTransfer, ItemTransfer.class, HttpMethod.POST, true);

        //transfer the money to the seller
        MoneyTransfer moneyTransfer = new MoneyTransfer(foundSenderBankAccount.getId(), foundReceiverBankAccount.getId(), offer.getPrice());
        restCall.callWithStatusCheck(TRANSFER_MONEY, moneyTransfer, MoneyTransfer.class, HttpMethod.POST, true);

        //change sold and news bools in offer
        offer.setSold(true);
        offer.setNews(true);

        //update buyTokens
        foundAccount.setBuyTokens(foundAccount.getBuyTokens() - 1);
        accountRepository.save(foundAccount);

        offerRepository.save(offer);
    }

    @Override
    public Iterable<Offer> getAllOffers() {
        return offerRepository.findTop10ByOrderByOfferId();
    }

    //region Generic exception methods

    private void checkOfferValuesNotEmpty(Offer offer){
        if(offer.getItemId() <= 0 || offer.getPrice() == null || offer.getPrice() <= 0 || offer.getSenderId() <= 0 || offer.getSenderBankNumber() <= 0){
            throw new IllegalArgumentException("Values cannot be null or negative");
        }
        if(offer.getOfferName() == null || Strings.isNullOrEmpty(offer.getOfferName())){
            String itemName = restCall.callWithStatusCheck(INVENTORY_ITEM_GET + offer.getItemId(), null, ItemWrapper.class, HttpMethod.GET, true).getBody().getName();
            offer.setOfferName(itemName);
        }
    }

    private Offer checkOfferExists(int offerId){
        Optional<Offer> foundOffer = offerRepository.findById(offerId);
        if(!foundOffer.isPresent()){
            throw new IllegalArgumentException("Offer with id: " + offerId + " not found");
        }

        return foundOffer.get();
    }

    private Offer checkAccountHasOffer(int accountId, int offerId){
        Offer foundOffer = checkOfferExists(offerId);
        if(foundOffer.getSenderId() != accountId){
            throw new IllegalArgumentException("User with id: " + accountId + "doesn't have acces to this offer");
        }

        return foundOffer;
    }

    private Account checkAccountExists(int accountId){
        Optional<Account> foundAccount = accountRepository.findById(accountId);
        if(!foundAccount.isPresent()){
            throw new IllegalArgumentException("account with id: " + accountId + " not found");
        }
        return foundAccount.get();
    }

    private BankAccountWrapper checkBankNumber(int accountId, int bankNumber){
        //check if bankNumber is not null or negative
        if(bankNumber <= 0){
            throw new IllegalArgumentException("the bank number is not valid");
        }

        //get bank account from the Bank API
        List<BankAccountWrapper> bankAccounts = new ArrayList<>(
                Arrays.asList(Objects.requireNonNull(restCall.callWithStatusCheck(GET_BANK_ACCOUNTS + accountId, null, BankAccountWrapper[].class, HttpMethod.GET, true).getBody()))
        );

        //check if bank accounts are not null or empty
        if(bankAccounts.size() == 0){
            throw new IllegalArgumentException("no bank accounts found");
        }

        //check if the given bankNumber is a legit bank account that the user posses
        Optional<BankAccountWrapper> foundBankAccount = bankAccounts.stream().filter(bankAccount -> bankAccount.getBankNumber() == bankNumber).findFirst();
        if(!foundBankAccount.isPresent()){
            throw new IllegalArgumentException("no bank account found with the bank number: " + bankNumber);
        }

        return foundBankAccount.get();
    }

    //endregion
}
