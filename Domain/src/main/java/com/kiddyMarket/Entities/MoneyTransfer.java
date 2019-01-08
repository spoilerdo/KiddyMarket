package com.kiddyMarket.Entities;

public class MoneyTransfer {
    private int senderId;
    private int receiverId;
    private float price;

    public MoneyTransfer(int senderId, int receiverId, float price) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.price = price;
    }
}
