package com.kiddyMarket.Entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Account extends ResourceSupport {
    @Id
    private int accountId;
    private String username;
    private float buyTokens;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "AccountOffer",
            joinColumns = {@JoinColumn(name = "AccountId")},
            inverseJoinColumns = {@JoinColumn(name = "OfferId")}
    )
    private Set<Offer> offers = new HashSet<>();

    public Account() {}

    public Account(int accountId, String username) {
        this.accountId = accountId;
        this.username = username;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setBuyTokens(float buyTokens) {
        this.buyTokens = buyTokens;
    }

    public float getBuyTokens() {
        return buyTokens;
    }

    public Set<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }
}
