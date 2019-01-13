package com.kiddyMarket.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kiddyMarket.Entities.Enums.Quality;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
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
    private int senderBankNumber;
    private String offerName;
    private Float price;
    private boolean sold = false;
    private boolean news = true;
    private Date offerCreated = new Date();
    private Quality quality;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "offers")
    private Set<Account> accounts = new HashSet<>();

    public Offer() {
    }

    public Offer(int itemId, int senderId, int senderBankNumber, Float price) {
        this.itemId = itemId;
        this.senderId = senderId;
        this.senderBankNumber = senderBankNumber;
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

    public int getSenderBankNumber() {
        return senderBankNumber;
    }

    public void setSenderBankNumber(int senderBankNumber) {
        this.senderBankNumber = senderBankNumber;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public boolean isNews() {
        return news;
    }

    public void setNews(boolean news) {
        this.news = news;
    }

    public Date getOfferCreated() {
        return offerCreated;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public Quality getQuality() {
        return quality;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }
}