package com.kiddyMarket.Controllers;

import com.kiddyMarket.Entities.Offer;
import com.kiddyMarket.Entities.Wrappers.LoginWrapper;
import com.kiddyMarket.Entities.Wrappers.OfferItemWrapper;
import com.kiddyMarket.LogicInterfaces.IOfferLogic;
import com.kiddyMarket.Wrapper.OfferAcceptWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/offers")
public class OfferController {
    private IOfferLogic offerLogic;

    @Autowired
    public OfferController(IOfferLogic offerLogic){
        this.offerLogic = offerLogic;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<OfferItemWrapper> getOffer(@PathVariable("id") int offerId){
        return new ResponseEntity<>(offerLogic.getOfferDetails(offerId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Offer> createOffer(Authentication user, @RequestBody Offer offer){
        Offer createdOffer = offerLogic.createOffer(user, offer);
        return new ResponseEntity<>(createdOffer, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteOffer(Authentication user, @PathVariable("id") int offerId){
        offerLogic.deleteOffer(user, offerId);
    }

    @PutMapping
    public ResponseEntity<Offer> updateOffer(Authentication user, @RequestBody Offer offer){
        Offer updatedOffer = offerLogic.updateOffer(user, offer);
        return new ResponseEntity<>(updatedOffer, HttpStatus.OK);
    }

    @PostMapping(path = "/accept")
    @ResponseStatus(HttpStatus.OK)
    public void acceptOffer(Authentication user, @RequestBody OfferAcceptWrapper wrapper){
        offerLogic.acceptOffer(user, wrapper.getOffer(), wrapper.getBankNumber());
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Iterable<Offer>> getAllOffers(){
        Iterable<Offer> offers = offerLogic.getAllOffers();
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }
}
