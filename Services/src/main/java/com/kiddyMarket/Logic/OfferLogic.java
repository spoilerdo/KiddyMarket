package com.kiddyMarket.Logic;

import com.kiddyMarket.DataInterfaces.IOfferRepository;
import com.kiddyMarket.Entities.*;
import com.kiddyMarket.Logic.Helper.RestCallLogic;
import com.kiddyMarket.LogicInterfaces.IOfferLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.kiddyMarket.Logic.Constants.APIConstants.*;

@Service
public class OfferLogic implements IOfferLogic {
    private IOfferRepository offerRepository;
    private RestCallLogic restCall;

    @Autowired
    public OfferLogic(IOfferRepository offerRepository, RestCallLogic restCall){
        this.offerRepository = offerRepository;
        this.restCall = restCall;
    }

    @Override
    public Offer createOffer(Offer offer) {
        //check if user exists in the system
        int accountId = checkUserExists();

        //check if all offer values are not null
        checkOfferValuesEmpty(offer);

        offer.setSenderId(accountId);

        return offerRepository.save(offer);
    }

    @Override
    public void deleteOffer(int offerId) {
        //check if user exists in the system
        int accountId = checkUserExists();

        //check if offer is found in the system
        Offer offerFromDb = checkOfferExistsInDb(offerId);

        //check if user owns the offer
        checkUserOwnsOffer(accountId, offerFromDb);

        offerRepository.delete(offerFromDb);
    }

    @Override
    public Offer updateOffer(Offer offer) {
        //check is user exists in the system
        int accountId = checkUserExists();

        //check if offer values are not null
        checkOfferValuesEmpty(offer);

        //check if offer is found in the system
        Offer offerFromDb = checkOfferExistsInDb(offer.getOfferId());

        //check if user owns the offer
        checkUserOwnsOffer(accountId, offerFromDb);

        return offerRepository.save(offer);
    }

    @Override
    public void acceptOffer(Offer offer) {
        //check if the account is found in the system
        int accountId = checkUserExists();

        //check if offer is found in the system
        checkOfferExistsInDb(offer.getOfferId());

        //transfer the item to the buyer
        ItemTransfer itemTransfer = new ItemTransfer(offer.getSenderId(), accountId, offer.getItemId());
        restCall.postCall(TRANSFER_ITEM, itemTransfer.toString(), ItemTransfer.class);

        //transfer the money to the seller
        MoneyTransfer moneyTransfer = new MoneyTransfer(accountId, offer.getSenderId(), offer.getPrice());
        restCall.postCall(TRANSFER_MONEY, moneyTransfer.toString(), MoneyTransfer.class);
    }

    @Override
    public Iterable<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    //region Generic exception methods

    private void checkOfferValuesEmpty(Offer offer){
        if(offer.getItemId() == 0 || offer.getPrice() == null || offer.getPrice() <= 0){
            throw new IllegalArgumentException("Values cannot be null");
        }
    }

    private Offer checkOfferExistsInDb(int offerId){
        Optional<Offer> foundOffer = offerRepository.findById(offerId);
        if(!foundOffer.isPresent()){
            throw new IllegalArgumentException("Offer with id: " + offerId + " not found");
        }

        return foundOffer.get();
    }

    private int checkUserExists() {
        return restCall.getCallWithStatusCheck(AUTHCALL, Account.class).getBody().getAccountId();
    }

    private void checkUserOwnsOffer(int accountId, Offer offer){
        if(offer.getSenderId() != accountId){
            throw new IllegalArgumentException("User with id: " + accountId + "doesn't have acces to this offer");
        }
    }

    //endregion
}
