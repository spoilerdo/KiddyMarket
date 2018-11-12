package com.kiddyMarket.Logic;

import com.kiddyMarket.DataInterfaces.IOfferRepository;
import com.kiddyMarket.Entities.Offer;
import com.kiddyMarket.LogicInterfaces.IOfferLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OfferLogic implements IOfferLogic {
    private IOfferRepository offerRepository;

    @Autowired
    public OfferLogic(IOfferRepository offerRepository){
        this.offerRepository = offerRepository;
    }

    @Override
    public Offer createOffer(Offer offer) {
        //check if all offer values are not null
        checkOfferValuesEmpty(offer);

        return offerRepository.save(offer);
    }

    @Override
    public void deleteOffer(int offerId) {
        //check if offer is found in the system
        Offer offerFromDb = checkofferExistsInDb(offerId);

        offerRepository.delete(offerFromDb);
    }

    @Override
    public Offer updateOffer(Offer offer) {
        //check if offer values are not null
        checkOfferValuesEmpty(offer);

        //check if offer is found in the system
        checkofferExistsInDb(offer.getOfferId());

        return offerRepository.save(offer);
    }

    @Override
    public void acceptOffer(Offer offer, int accountId) {
        //check if the account is found in the system

        //check if offer values are not null
        checkOfferValuesEmpty(offer);

        //check if offer is found in the system
        checkofferExistsInDb(offer.getOfferId());

        //TODO: make a transaction in the inventory API etc
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

    private Offer checkofferExistsInDb(int offerId){
        Optional<Offer> foundOffer = offerRepository.findById(offerId);
        if(!foundOffer.isPresent()){
            throw new IllegalArgumentException("Offer with id: " + offerId + " not found");
        }

        return foundOffer.get();
    }

    //endregion
}
