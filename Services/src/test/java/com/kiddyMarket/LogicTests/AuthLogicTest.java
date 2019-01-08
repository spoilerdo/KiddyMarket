package com.kiddyMarket.LogicTests;

import com.kiddyMarket.DataInterfaces.IAccountRepository;
import com.kiddyMarket.Entities.Account;
import com.kiddyMarket.Logic.AuthLogic;
import com.kiddyMarket.Logic.OfferLogic;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthLogicTest {

    //Add exception dependency
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    //Mock repos
    @Mock
    private IAccountRepository accountRepository;

    //The logic you want to test injected with the repo mocks
    @InjectMocks
    private AuthLogic _logic;

    @Before //sets up the mock
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadUserByUsernameValidHasNoAccount(){
        Account dymmyAccount = new Account(1, "dummy");

        _logic.loadUserById(dymmyAccount.getAccountId(), dymmyAccount.getUsername());

        verify(accountRepository, times(1)).save(dymmyAccount);
    }

    @Test
    public void testLoadUserByUsernameValidHasAccount(){
        Account dymmyAccount = new Account(1, "dummy");

        _logic.loadUserById(dymmyAccount.getAccountId(), dymmyAccount.getUsername());
    }
}
