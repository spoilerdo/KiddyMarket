package com.kiddyMarket.LogicTests;

import com.kiddyMarket.DataInterfaces.IAccountRepository;
import com.kiddyMarket.DataInterfaces.IOfferRepository;
import com.kiddyMarket.Entities.Account;
import com.kiddyMarket.Entities.Offer;
import com.kiddyMarket.Entities.Wrappers.AccountFormWrapper;
import com.kiddyMarket.Entities.Wrappers.AccountWrapper;
import com.kiddyMarket.Entities.Wrappers.BankAccountWrapper;
import com.kiddyMarket.Logic.AccountLogic;
import com.kiddyMarket.Logic.Helper.RestCallLogic;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static com.kiddyMarket.Logic.Constants.APIConstants.AUTH_ACCOUNTS;
import static com.kiddyMarket.Logic.Constants.APIConstants.BANKCALL;
import static com.kiddyMarket.Logic.Constants.APIConstants.INVENTORY_ACCOUNTS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountLogicTest {

    //Add exception dependency
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    //Mock repos
    @Mock
    private IAccountRepository accountRepository;
    @Mock
    private IOfferRepository offerRepository;
    @Mock
    private RestCallLogic restCallLogic;

    //The logic you want to test injected with the repo mocks
    @InjectMocks
    private AccountLogic _logic;

    @Before //sets up the mock
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateBankAccountValid(){
        AccountFormWrapper accountFormWrapper = new AccountFormWrapper("dummy", "1234", "dummy@test.com", "1234", "dummyBank");

        AccountWrapper accountResult = new AccountWrapper("dummyresult", "1234", "dummy@test.com", "1234");
        BankAccountWrapper bankBankAccount = new BankAccountWrapper(accountFormWrapper.getBankName());

        when(restCallLogic.call(AUTH_ACCOUNTS + accountFormWrapper.getUsername(), null, AccountWrapper.class, HttpMethod.GET, false)).thenReturn(ResponseEntity.noContent().build());
        when(restCallLogic.callWithStatusCheck(eq(AUTH_ACCOUNTS), any(), eq(AccountWrapper.class), eq(HttpMethod.POST), eq(false))).thenReturn(ResponseEntity.ok(accountResult));
        when(restCallLogic.callWithStatusCheck(eq(BANKCALL), any(), eq(BankAccountWrapper.class), eq(HttpMethod.POST), eq(false))).thenReturn(ResponseEntity.ok(bankBankAccount));

        Assert.assertNotNull(_logic.createBankAccount(accountFormWrapper));
    }

    @Test
    public void testCreateBankAccountInvalidFormWrapperEmpty(){
        AccountFormWrapper accountForm = new AccountFormWrapper("", "1234", "dummy@test.com", "", "dummyBank");

        exception.expect(IllegalArgumentException.class);

        _logic.createBankAccount(accountForm);
    }

    @Test
    public void testDeleteBankAccountValid(){
        int accountId = 1;
        AccountWrapper dummyAccount = new AccountWrapper("dummy", "1234", "dummy@test.com", "1234");

        when(restCallLogic.callWithStatusCheck(AUTH_ACCOUNTS + accountId, null, AccountWrapper.class, HttpMethod.GET, true)).thenReturn(ResponseEntity.ok(dummyAccount));
        when(restCallLogic.call(AUTH_ACCOUNTS + accountId, null, int.class, HttpMethod.DELETE, true)).thenReturn(ResponseEntity.ok().build());
        when(restCallLogic.call(INVENTORY_ACCOUNTS + accountId, null, int.class, HttpMethod.DELETE, true)).thenReturn(ResponseEntity.ok().build());

        _logic.deleteBankAccount(accountId);

        verify(accountRepository, times(1)).deleteById(accountId);
    }

    @Test
    public void testDeleteBankAccountInvalid(){
        int accountId = 1;

        when(restCallLogic.callWithStatusCheck(AUTH_ACCOUNTS + accountId, null, AccountWrapper.class, HttpMethod.GET, true)).thenReturn(ResponseEntity.noContent().build());

        exception.expect(IllegalArgumentException.class);

        _logic.deleteBankAccount(accountId);
    }

    @Test
    public void testGetOffersFromAccountValid(){
        Offer dummy1Offer = new Offer(1, 1, 1, 10f);
        dummy1Offer.setOfferName("dummy offer");
        dummy1Offer.setOfferId(0);

        Offer dummy2Offer = new Offer(1, 1, 1, 10f);
        dummy2Offer.setOfferName("dummy offer");
        dummy1Offer.setOfferId(1);

        List<Offer> offers = new ArrayList<>();
        offers.add(dummy1Offer);
        offers.add(dummy2Offer);

        Account dummyAccount = new Account(1, "dummyAccount");
        dummyAccount.setOffers(offers);

        when(accountRepository.findById(dummyAccount.getAccountId())).thenReturn(Optional.of(dummyAccount));

        Iterable<Offer> foundOffers = _logic.getOffersFromAccount(dummyAccount.getAccountId());

        Assert.assertTrue(foundOffers.equals(offers));
    }

    @Test
    public void testGetOfferFromAccountInvalid(){
        Account dummyAccount = new Account(1, "dummyAccount");

        when(accountRepository.findById(dummyAccount.getAccountId())).thenReturn(Optional.empty());

        exception.expect(IllegalArgumentException.class);

        _logic.getOffersFromAccount(dummyAccount.getAccountId());
    }

    @Test
    public void testGetNewOffersFromAccountValid(){
        Offer dummy1Offer = new Offer(1, 1, 1, 10f);
        dummy1Offer.setOfferName("dummy offer");
        dummy1Offer.setOfferId(0);
        dummy1Offer.setNews(true);

        Offer dummy2Offer = new Offer(1, 1, 1, 10f);
        dummy2Offer.setOfferName("dummy offer");
        dummy2Offer.setOfferId(1);
        dummy2Offer.setNews(false);

        List<Offer> offers = new ArrayList<>();
        offers.add(dummy1Offer);
        offers.add(dummy2Offer);

        Account dummyAccount = new Account(1, "dummyAccount");
        dummyAccount.setOffers(offers);

        when(accountRepository.findById(dummyAccount.getAccountId())).thenReturn(Optional.of(dummyAccount));

        Iterable<Offer> foundOffers = _logic.getNewOffersFromAccount(dummyAccount.getAccountId());

        Assert.assertNotEquals(foundOffers, offers);
    }

    @Test
    public void testGetNewOffersFromAccountInvalid(){
        Account dummyAccount = new Account(1, "dummyAccount");

        when(accountRepository.findById(dummyAccount.getAccountId())).thenReturn(Optional.empty());

        exception.expect(IllegalArgumentException.class);

        _logic.getNewOffersFromAccount(dummyAccount.getAccountId());
    }

    @Test
    public void testChangeOfferNewsValid(){
        Offer dummy1Offer = new Offer(1, 1, 1, 10f);
        dummy1Offer.setOfferName("dummy offer");
        dummy1Offer.setOfferId(0);
        dummy1Offer.setNews(true);

        Offer dummy2Offer = new Offer(1, 1, 1, 10f);
        dummy2Offer.setOfferName("dummy offer");
        dummy2Offer.setOfferId(1);
        dummy2Offer.setNews(false);

        List<Offer> offers = new ArrayList<>();
        offers.add(dummy1Offer);
        offers.add(dummy2Offer);

        Account dummyAccount = new Account(1, "dummyAccount");
        dummyAccount.setOffers(offers);

        when(accountRepository.findById(dummyAccount.getAccountId())).thenReturn(Optional.of(dummyAccount));

        _logic.changeOfferNews(dummyAccount.getAccountId());

        verify(accountRepository, times(1)).save(dummyAccount);
        verify(offerRepository, times(1)).deleteOfferBySenderIdAndSold(dummyAccount.getAccountId(), true);
    }

    @Test
    public void testChangeOfferNewsInvalid(){
        Account dummyAccount = new Account(1, "dummyAccount");

        when(accountRepository.findById(dummyAccount.getAccountId())).thenReturn(Optional.empty());

        exception.expect(IllegalArgumentException.class);

        _logic.changeOfferNews(dummyAccount.getAccountId());
    }
}
