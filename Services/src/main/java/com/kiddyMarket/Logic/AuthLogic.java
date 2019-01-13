package com.kiddyMarket.Logic;

import com.kiddyMarket.DataInterfaces.IAccountRepository;
import com.kiddyMarket.Entities.Account;
import com.kiddyMarket.Entities.JwtEntities.JwtUserPrincipal;
import com.kiddyMarket.Logic.Helper.RestCallLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthLogic {
    private IAccountRepository accountRepository;

    @Autowired
    public AuthLogic(IAccountRepository accountRepository, RestCallLogic restCall){
        this.accountRepository = accountRepository;
    }

    public UserDetails loadUserById(int UserId, String username) throws UsernameNotFoundException {

        if(username.equals("")){
            throw new UsernameNotFoundException("username cannot be empty");
        }

        Optional<Account> foundAccount = accountRepository.findByUsername(username);

        //check if user has logged in to the Market before, if not create new account
        Account account;
        if(!foundAccount.isPresent()){
            account = accountRepository.save(new Account(UserId, username));
        } else{
            account = foundAccount.get();
        }

        return new JwtUserPrincipal(account);
    }
}
