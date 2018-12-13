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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
        return null;
    }
}
