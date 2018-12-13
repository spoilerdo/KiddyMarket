package com.kiddyMarket.Entities.Wrappers;

public class AccountWrapper {
    //This entity has all the information in order to make a account from the Bank-API
    private String username;
    private String password;
    private String email;
    private String phonenr;

    public AccountWrapper(String username, String password, String email, String phonenr) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phonenr = phonenr;
    }
}
