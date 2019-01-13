package com.kiddyMarket.LogicTests;

import com.kiddyMarket.DataInterfaces.IAccountRepository;
import com.kiddyMarket.Entities.Account;
import com.kiddyMarket.Logic.AuthLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
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
        Account dummyAccount = new Account(1, "dummy");

        when(accountRepository.findByUsername(dummyAccount.getUsername())).thenReturn(Optional.empty());
        when(accountRepository.save(dummyAccount)).thenReturn(dummyAccount);

        UserDetails user = _logic.loadUserById(dummyAccount.getAccountId(), dummyAccount.getUsername());

        verify(accountRepository, times(1)).save(dummyAccount);
        Assert.assertNotEquals(user.getUsername(), "");
    }

    @Test
    public void testLoadUserByUsernameInvalid(){
        Account dummyAccount = new Account(1, "");

        exception.expect(UsernameNotFoundException.class);

        _logic.loadUserById(dummyAccount.getAccountId(), dummyAccount.getUsername());
    }

    @Test
    public void testLoadUserByUsernameValidHasAccount(){
        Account dummyAccount = new Account(1, "dummy");

        when(accountRepository.findByUsername(dummyAccount.getUsername())).thenReturn(Optional.of(dummyAccount));

        UserDetails user = _logic.loadUserById(dummyAccount.getAccountId(), dummyAccount.getUsername());

        verify(accountRepository, times(0)).save(dummyAccount);
        Assert.assertNotEquals(user.getUsername(), "");
    }
}
