package com.kiddyMarket.Logic;

import com.kiddyMarket.DataInterfaces.IAccountRepository;
import com.kiddyMarket.Entities.Account;
import com.kiddyMarket.Logic.Helper.RestCallLogic;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import static java.util.Collections.emptyList;
import java.util.Optional;

import static com.kiddyMarket.Logic.Constants.APIConstants.*;

@Service
public class AuthLogic implements UserDetailsService {
    private HttpServletRequest request;
    private IAccountRepository accountRepository;
    private RestCallLogic restCall;

    @Autowired
    public AuthLogic(HttpServletRequest request, IAccountRepository accountRepository, RestCallLogic restCall){
        this.request = request;
        this.accountRepository = accountRepository;
        this.restCall = restCall;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseEntity<JSONObject> responseEntityAccount = restCall.getCall(AUTHCALL, JSONObject.class);

        JSONObject account = responseEntityAccount.getBody();
        try{
            int accountId = account.getInt("id");
            Optional<Account> foundAccount = accountRepository.findById(accountId);

            if(!foundAccount.isPresent()){
                //save user to market db if it doesn't exist yet
                Account newAccount = new Account();
                newAccount.setAccountId(accountId);
                accountRepository.save(newAccount);
            }

            //return spring-security user
            return new User(account.getString("username"), account.getString("password"), emptyList());
        } catch (JSONException e){
            throw new UsernameNotFoundException(username);
        }
    }
}
