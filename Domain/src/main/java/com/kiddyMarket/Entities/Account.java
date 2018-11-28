package com.kiddyMarket.Entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Account extends ResourceSupport {
    @Id
    private int accountId;
    private float balance;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "AccountItem",
            joinColumns = {@JoinColumn(name = "AccountId")},
            inverseJoinColumns = {@JoinColumn(name = "OfferId")}
    )
    private List<Offer> offers = new ArrayList<>();

    public Account() {}

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getAccountId() {
        return accountId;
    }
}
