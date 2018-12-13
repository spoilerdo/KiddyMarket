package com.kiddyMarket.Entities.Wrappers;

public class BankAccountWrapper {
    //This entity has all the information in order to make a bank-account from the Bank-API
    private int id;
    private int bankNumber;
    private String name;
    private float balance;

    public BankAccountWrapper(){}

    public BankAccountWrapper(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setBankNumber(int bankNumber) {
        this.bankNumber = bankNumber;
    }

    public int getBankNumber() {
        return bankNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getBalance() {
        return balance;
    }
}
