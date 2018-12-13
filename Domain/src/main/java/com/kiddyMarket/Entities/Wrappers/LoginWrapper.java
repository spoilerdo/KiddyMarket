package com.kiddyMarket.Entities.Wrappers;

public class LoginWrapper {
    //This entity has all the information in order to login to an API
    private String username;
    private String password;

    public LoginWrapper(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
