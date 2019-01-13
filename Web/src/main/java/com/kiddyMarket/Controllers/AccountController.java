package com.kiddyMarket.Controllers;

import com.kiddyMarket.Entities.Offer;
import com.kiddyMarket.Entities.Wrappers.AccountFormWrapper;
import com.kiddyMarket.Entities.Wrappers.AccountWrapper;
import com.kiddyMarket.LogicInterfaces.IAccountLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private IAccountLogic accountLogic;

    @Autowired
    public AccountController(IAccountLogic accountLogic) {
        this.accountLogic = accountLogic;
    }

    @PostMapping
    public ResponseEntity<AccountWrapper> createAccount(@RequestBody AccountFormWrapper accountForm){
        AccountWrapper createdOffer = accountLogic.createBankAccount(accountForm);
        return new ResponseEntity<>(createdOffer, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(@PathVariable("id") int accountId){
        accountLogic.deleteBankAccount(accountId);
    }

    @GetMapping(path = "/tokens/{id}")
    public ResponseEntity<Integer> getBuyTokens(@PathVariable("id") int accountId){
        int tokens = accountLogic.getBuyTokens(accountId);
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    @GetMapping(path = "/offers/{id}")
    public ResponseEntity<List<Offer>> getAllOffersFromAccount(@PathVariable("id") int accountId){
        List<Offer> offers = accountLogic.getOffersFromAccount(accountId);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @GetMapping(path = "/offers/new/{id}")
    public ResponseEntity<Iterable<Offer>> getAllNewOffersFromAccount(@PathVariable("id") int accountId){
        Iterable<Offer> offers = accountLogic.getNewOffersFromAccount(accountId);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @PutMapping(path = "/offers/new/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateOfferNews(@PathVariable("id") int accountId){
        accountLogic.changeOfferNews(accountId);
    }
}
