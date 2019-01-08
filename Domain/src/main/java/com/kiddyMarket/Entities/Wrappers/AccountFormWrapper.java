package com.kiddyMarket.Entities.Wrappers;

public class AccountFormWrapper {
    //This entity has all the information needed to create all the API accounts
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String bankName;

    public AccountFormWrapper(String username, String password, String email, String phoneNumber, String bankName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.bankName = bankName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBankName() {
        return bankName;
    }
}
