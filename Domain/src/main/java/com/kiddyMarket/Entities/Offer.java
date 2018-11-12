package com.kiddyMarket.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Offer extends ResourceSupport{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int offerId;
    private int itemId;
    private int senderId;
    private Float price;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "offers")
    private Set<Account> accounts = new HashSet<>();

    public Offer() {}

    public Offer(int itemId, int senderId, Float price) {
        this.itemId = itemId;
        this.senderId = senderId;
        this.price = price;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}