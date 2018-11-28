package com.kiddyMarket.Logic;

import com.kiddyMarket.Entities.Wrappers.AccountFormWrapper;
import com.kiddyMarket.Entities.Wrappers.AccountWrapper;
import com.kiddyMarket.Entities.Wrappers.BankAccountWrapper;
import com.kiddyMarket.Logic.Helper.RestCallLogic;
import com.kiddyMarket.LogicInterfaces.IAccountLogic;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.kiddyMarket.Logic.Constants.APIConstants.*;

@Service
public class AccountLogic implements IAccountLogic {
    //TODO: finish this logic file
    private RestCallLogic restCall;

    @Autowired
    public AccountLogic(RestCallLogic restCall) {
        this.restCall = restCall;
    }

    @Override
    public void CreateBankAccount(AccountFormWrapper accountFormWrapper) {
        //check if required values are filled in
        if(Strings.isNullOrEmpty(accountFormWrapper.getUsername()) || Strings.isNullOrEmpty(accountFormWrapper.getPassword()) || Strings.isNullOrEmpty(accountFormWrapper.getEmail())){
            throw new IllegalArgumentException("Values cannot be null");
        }

        //check if username already exists
        if(restCall.getCall(AUTHCALL + accountFormWrapper.getUsername(), AccountWrapper.class).getStatusCode() == HttpStatus.OK){
            throw new IllegalArgumentException("Username already in use");
        }

        //create account for Bank-API
        AccountWrapper account = new AccountWrapper(
                accountFormWrapper.getUsername(),
                accountFormWrapper.getPassword(),
                accountFormWrapper.getEmail(),
                accountFormWrapper.getPhoneNumber()
        );
        restCall.postCall(AUTHCALL, account.toString(), AccountWrapper.class).getBody();

        //create bank-account for Bank-API //TODO: this will be redundant if Tygo fixed the bank API
        BankAccountWrapper bankAccount = new BankAccountWrapper(accountFormWrapper.getBankName());
        restCall.postCall(BANKCALL, bankAccount.toString(), BankAccountWrapper.class).getBody();
    }
}
