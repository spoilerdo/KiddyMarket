package com.kiddyMarket.Logic;

import com.kiddyMarket.DataInterfaces.IAccountRepository;
import com.kiddyMarket.Entities.Account;
import net.minidev.json.JSONValue;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import static java.util.Collections.emptyList;
import java.util.Optional;

import static com.kiddyMarket.Logic.Constants.AuthConstants.*;

@Service
public class AuthLogic implements UserDetailsService {
    private HttpServletRequest request;
    private IAccountRepository accountRepository;

    @Autowired
    public AuthLogic(HttpServletRequest request, IAccountRepository accountRepository){
        this.request = request;
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JSONObject account = getAccountFromAPI(username);

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

    private JSONObject getAccountFromAPI(String username){
        //set the requested headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHHEADER, request.getHeader(AUTHHEADER));

        //make a empty entity (in this case it will be filled with an account later on)
        HttpEntity<?> httpEntity = new HttpEntity<>("", headers);

        //get the entity from an API
        RestTemplate restCall = new RestTemplate();
        ResponseEntity<String> response = restCall.exchange(AUTHCALL + username, HttpMethod.GET, httpEntity, String.class);

        //check if status code is correct
        if(response.getStatusCode() != HttpStatus.OK){
            throw new UsernameNotFoundException(username);
        }

        //convert entity to json and return
        return (JSONObject) JSONValue.parse(response.getBody());
    }
}
