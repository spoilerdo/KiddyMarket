package com.kiddyMarket.Entities;

public class ItemTransfer {
    private int senderId;
    private int receiverId;
    private int itemId;

    public ItemTransfer(int senderId, int receiverId, int itemId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.itemId = itemId;
    }
}
