package com.kiddyMarket.Controllers;

import com.kiddyMarket.Entities.Offer;
import com.kiddyMarket.LogicInterfaces.IOfferLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/offer")
public class OfferController {
    private IOfferLogic offerLogic;

    @Autowired
    public OfferController(IOfferLogic offerLogic){
        this.offerLogic = offerLogic;
    }

    @PostMapping(path = "/")
    public ResponseEntity<Offer> createOffer(@RequestBody Offer offer){
        Offer createdOffer = offerLogic.createOffer(offer);
        return new ResponseEntity<>(createdOffer, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteOffer(@PathVariable("id") int offerId){
        offerLogic.deleteOffer(offerId);
    }

    @PutMapping(path = "/")
    public ResponseEntity<Offer> updateOffer(@RequestBody Offer offer){
        Offer updatedOffer = offerLogic.updateOffer(offer);
        return new ResponseEntity<>(updatedOffer, HttpStatus.OK);
    }

    @PostMapping(path = "/accept")
    @ResponseStatus(HttpStatus.OK)
    public void acceptOffer(@RequestBody Offer offer){
        offerLogic.acceptOffer(offer);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Iterable<Offer>> getAllOffers(){
        Iterable<Offer> offers = offerLogic.getAllOffers();
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

}
