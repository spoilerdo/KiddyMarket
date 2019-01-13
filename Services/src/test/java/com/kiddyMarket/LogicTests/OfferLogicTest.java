package com.kiddyMarket.LogicTests;

import com.kiddyMarket.DataInterfaces.IAccountRepository;
import com.kiddyMarket.DataInterfaces.IOfferRepository;
import com.kiddyMarket.Entities.Account;
import com.kiddyMarket.Entities.Enums.Condition;
import com.kiddyMarket.Entities.Enums.Quality;
import com.kiddyMarket.Entities.Offer;
import com.kiddyMarket.Entities.Wrappers.BankAccountWrapper;
import com.kiddyMarket.Entities.Wrappers.ItemWrapper;
import com.kiddyMarket.Logic.Helper.RestCallLogic;
import com.kiddyMarket.Logic.OfferLogic;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static com.kiddyMarket.Logic.Constants.APIConstants.GET_BANK_ACCOUNTS;
import static com.kiddyMarket.Logic.Constants.APIConstants.INVENTORY_ITEM_GET;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OfferLogicTest {

    //Add exception dependency
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    //Mock repos
    @Mock
    private IOfferRepository offerRepository;
    @Mock
    private IAccountRepository accountRepository;
    @Mock
    private RestCallLogic restCallLogic;

    //The logic you want to test injected with the repo mocks
    @InjectMocks
    private OfferLogic _logic;

    @Before //sets up the mock
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateOfferValid(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Account dummyAccount = new Account(1, "dummyAccount");

        BankAccountWrapper dummy1BankAccount = new BankAccountWrapper("test account");
        dummy1BankAccount.setBankNumber(1234);

        Offer dummyOffer = new Offer(1, dummyAccount.getAccountId(), dummy1BankAccount.getBankNumber(), 10f);
        dummyOffer.setOfferName("dummy offer");

        when(dummyUser.getPrincipal()).thenReturn(dummyAccount.getAccountId());
        when(restCallLogic.callWithStatusCheck(GET_BANK_ACCOUNTS + dummyAccount.getAccountId(), null, BankAccountWrapper[].class, HttpMethod.GET, true))
                .thenReturn(ResponseEntity.ok(new BankAccountWrapper[]{dummy1BankAccount}));
        when(restCallLogic.callWithStatusCheck(INVENTORY_ITEM_GET + dummyOffer.getItemId(), null, ItemWrapper.class, HttpMethod.GET, false))
                .thenReturn(ResponseEntity.ok(new ItemWrapper("test item", "d", Condition.FN, Quality.Ancient, 1f)));
        when(accountRepository.findById(dummyAccount.getAccountId())).thenReturn(Optional.of(dummyAccount));
        when(offerRepository.save(dummyOffer)).thenReturn(dummyOffer);

        _logic.createOffer(dummyUser, dummyOffer);

        verify(offerRepository, times(1)).save(dummyOffer);
    }

    @Test
    public void testCreateOfferInvalidEmptyOfferValues(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Account dummyAccount = new Account(1, "dummyAccount");

        Offer dummyOffer = new Offer(1, 0, 0, 0f);

        when(dummyUser.getPrincipal()).thenReturn(dummyAccount.getAccountId());

        exception.expect(IllegalArgumentException.class);

        _logic.createOffer(dummyUser, dummyOffer);
    }

    @Test
    public void testCreateOfferInvalidNoAccount(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Account dummyAccount = new Account(1, "dummyAccount");

        BankAccountWrapper dummy1BankAccount = new BankAccountWrapper("test account");
        dummy1BankAccount.setBalance(100f);
        dummy1BankAccount.setBankNumber(1234);

        Offer dummyOffer = new Offer(1, dummyAccount.getAccountId(), dummy1BankAccount.getBankNumber(), 10f);
        dummyOffer.setOfferName("dummy offer");

        when(dummyUser.getPrincipal()).thenReturn(dummyAccount.getAccountId());
        when(restCallLogic.callWithStatusCheck(GET_BANK_ACCOUNTS + dummyAccount.getAccountId(), null, BankAccountWrapper[].class, HttpMethod.GET, true))
                .thenReturn(ResponseEntity.ok(new BankAccountWrapper[]{dummy1BankAccount}));
        when(accountRepository.findById(dummyAccount.getAccountId())).thenReturn(Optional.empty());

        exception.expect(IllegalArgumentException.class);

        _logic.createOffer(dummyUser, dummyOffer);
    }

    @Test
    public void testCreateOfferInvalidNoBankAccount(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Account dummyAccount = new Account(1, "dummyAccount");

        BankAccountWrapper dummy1BankAccount = new BankAccountWrapper("test account");
        dummy1BankAccount.setBankNumber(1234);

        Offer dummyOffer = new Offer(1, dummyAccount.getAccountId(), 100, 10f);
        dummyOffer.setOfferName("dummy offer");

        when(dummyUser.getPrincipal()).thenReturn(dummyAccount.getAccountId());
        when(restCallLogic.callWithStatusCheck(GET_BANK_ACCOUNTS + dummyAccount.getAccountId(), null, BankAccountWrapper[].class, HttpMethod.GET, true))
                .thenReturn(ResponseEntity.ok(new BankAccountWrapper[]{dummy1BankAccount}));

        exception.expect(IllegalArgumentException.class);

        _logic.createOffer(dummyUser, dummyOffer);
    }

    @Test
    public void testDeleteOfferValid(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Account dummyAccount = new Account(1, "dummyAccount");

        Offer dummyOffer = new Offer(1, dummyAccount.getAccountId(), 1, 10f);
        dummyOffer.setOfferName("dummy offer");
        dummyOffer.setSenderId(dummyAccount.getAccountId());

        when(dummyUser.getPrincipal()).thenReturn(dummyAccount.getAccountId());
        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.of(dummyOffer));

        _logic.deleteOffer(dummyUser, dummyOffer.getOfferId());

        verify(offerRepository, times(1)).delete(dummyOffer);
    }

    @Test
    public void testDeleteOfferInvalidAccountDoesntContainOffer(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Account dummyAccount = new Account(1, "dummyAccount");

        Offer dummyOffer = new Offer(1, 100, 1, 10f);
        dummyOffer.setOfferName("dummy offer");

        when(dummyUser.getPrincipal()).thenReturn(dummyAccount.getAccountId());
        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.of(dummyOffer));

        exception.expect(IllegalArgumentException.class);

        _logic.deleteOffer(dummyUser, dummyOffer.getOfferId());
    }

    @Test
    public void testDeleteOfferInvalidOfferNotPresent(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Account dummyAccount = new Account(1, "dummyAccount");

        Offer dummyOffer = new Offer(1, dummyAccount.getAccountId(), 1, 10f);
        dummyOffer.setOfferName("dummy offer");

        when(dummyUser.getPrincipal()).thenReturn(dummyAccount.getAccountId());
        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.empty());

        exception.expect(IllegalArgumentException.class);

        _logic.deleteOffer(dummyUser, dummyOffer.getOfferId());
    }

    @Test
    public void testUpdateOfferValid(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Account dummyAccount = new Account(1, "dummyAccount");

        Offer dummyOffer = new Offer(1, dummyAccount.getAccountId(), 1, 10f);
        dummyOffer.setOfferName("dummy offer");
        dummyOffer.setSenderId(dummyAccount.getAccountId());

        when(dummyUser.getPrincipal()).thenReturn(dummyAccount.getAccountId());
        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.of(dummyOffer));

        _logic.updateOffer(dummyUser, dummyOffer);

        verify(offerRepository, times(1)).save(dummyOffer);
    }

    @Test
    public void testUpdateOfferInvalid(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Account dummyAccount = new Account(1, "dummyAccount");

        Offer dummyOffer = new Offer(1, dummyAccount.getAccountId(), 1, 10f);
        dummyOffer.setOfferName("dummy offer");

        when(dummyUser.getPrincipal()).thenReturn(dummyAccount.getAccountId());
        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.empty());

        exception.expect(IllegalArgumentException.class);

        _logic.updateOffer(dummyUser, dummyOffer);
    }

    @Test
    public void testAcceptOfferValid(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Account dummyAccount = new Account(1, "dummyAccount");
        dummyAccount.setBuyTokens(1);

        BankAccountWrapper dummy1BankAccount = new BankAccountWrapper("test account");
        dummy1BankAccount.setBalance(100f);
        dummy1BankAccount.setBankNumber(1234);

        BankAccountWrapper dummy2BankAccount = new BankAccountWrapper("test account");
        dummy2BankAccount.setId(1);
        dummy2BankAccount.setBankNumber(5678);

        Offer dummyOffer = new Offer(1, 2, dummy2BankAccount.getBankNumber(), 10f);
        dummyOffer.setOfferName("dummy offer");

        when(dummyUser.getPrincipal()).thenReturn(dummyAccount.getAccountId());
        when(accountRepository.findById(dummyAccount.getAccountId())).thenReturn(Optional.of(dummyAccount));
        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.of(dummyOffer));

        when(restCallLogic.callWithStatusCheck(GET_BANK_ACCOUNTS + dummyAccount.getAccountId(), null, BankAccountWrapper[].class, HttpMethod.GET, true))
                .thenReturn(ResponseEntity.ok(new BankAccountWrapper[]{dummy1BankAccount}));
        when(restCallLogic.callWithStatusCheck(GET_BANK_ACCOUNTS + dummyOffer.getSenderId(), null, BankAccountWrapper[].class, HttpMethod.GET, true))
                .thenReturn(ResponseEntity.ok(new BankAccountWrapper[]{dummy2BankAccount}));

        _logic.acceptOffer(dummyUser, dummyOffer, dummy1BankAccount.getBankNumber());

        verify(offerRepository, times(1)).save(dummyOffer);
    }

    @Test
    public void testAcceptOfferInvalidAccountNotEnoughBuyTokens(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Account dummyAccount = new Account(1, "dummyAccount");

        BankAccountWrapper dummy1BankAccount = new BankAccountWrapper("test account");
        dummy1BankAccount.setBalance(100f);
        dummy1BankAccount.setBankNumber(1234);

        Offer dummyOffer = new Offer(1, 2, 3, 10f);
        dummyOffer.setOfferName("dummy offer");

        when(dummyUser.getPrincipal()).thenReturn(dummyAccount.getAccountId());
        when(accountRepository.findById(dummyAccount.getAccountId())).thenReturn(Optional.of(dummyAccount));

        exception.expect(IllegalArgumentException.class);

        _logic.acceptOffer(dummyUser, dummyOffer, dummy1BankAccount.getBankNumber());
    }

    @Test
    public void testAcceptOfferInvalidBankNumber(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Account dummyAccount = new Account(1, "dummyAccount");
        dummyAccount.setBuyTokens(1);

        BankAccountWrapper dummy1BankAccount = new BankAccountWrapper("test account");
        dummy1BankAccount.setBalance(100f);
        dummy1BankAccount.setBankNumber(-1);

        Offer dummyOffer = new Offer(1, 2, 3, 10f);
        dummyOffer.setOfferName("dummy offer");

        when(dummyUser.getPrincipal()).thenReturn(dummyAccount.getAccountId());
        when(accountRepository.findById(dummyAccount.getAccountId())).thenReturn(Optional.of(dummyAccount));
        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.of(dummyOffer));

        exception.expect(IllegalArgumentException.class);

        _logic.acceptOffer(dummyUser, dummyOffer, dummy1BankAccount.getBankNumber());
    }

    @Test
    public void testAcceptOfferInvalidNoBankAccountFound(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Account dummyAccount = new Account(1, "dummyAccount");
        dummyAccount.setBuyTokens(1);

        BankAccountWrapper dummy1BankAccount = new BankAccountWrapper("test account");
        dummy1BankAccount.setBalance(100f);
        dummy1BankAccount.setBankNumber(1234);

        Offer dummyOffer = new Offer(1, 2, 3, 10f);
        dummyOffer.setOfferName("dummy offer");

        when(dummyUser.getPrincipal()).thenReturn(dummyAccount.getAccountId());
        when(accountRepository.findById(dummyAccount.getAccountId())).thenReturn(Optional.of(dummyAccount));
        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.of(dummyOffer));

        when(restCallLogic.callWithStatusCheck(GET_BANK_ACCOUNTS + dummyAccount.getAccountId(), null, BankAccountWrapper[].class, HttpMethod.GET, true))
                .thenReturn(ResponseEntity.noContent().build());

        exception.expect(NullPointerException.class);

        _logic.acceptOffer(dummyUser, dummyOffer, dummy1BankAccount.getBankNumber());
    }

    @Test
    public void testAcceptOfferInvalidAccountDoesNotHaveAnyBankAccounts(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Account dummyAccount = new Account(1, "dummyAccount");
        dummyAccount.setBuyTokens(1);

        BankAccountWrapper dummy1BankAccount = new BankAccountWrapper("test account");
        dummy1BankAccount.setBalance(100f);
        dummy1BankAccount.setBankNumber(1234);


        Offer dummyOffer = new Offer(1, 2, 3, 10f);
        dummyOffer.setOfferName("dummy offer");

        when(dummyUser.getPrincipal()).thenReturn(dummyAccount.getAccountId());
        when(accountRepository.findById(dummyAccount.getAccountId())).thenReturn(Optional.of(dummyAccount));
        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.of(dummyOffer));

        when(restCallLogic.callWithStatusCheck(GET_BANK_ACCOUNTS + dummyAccount.getAccountId(), null, BankAccountWrapper[].class, HttpMethod.GET, true))
                .thenReturn(ResponseEntity.ok(new BankAccountWrapper[]{}));

        exception.expect(IllegalArgumentException.class);

        _logic.acceptOffer(dummyUser, dummyOffer, dummy1BankAccount.getBankNumber());
    }

    @Test
    public void testAcceptOfferInvalidNotEnoughBalance(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Account dummyAccount = new Account(1, "dummyAccount");
        dummyAccount.setBuyTokens(1);

        BankAccountWrapper dummy1BankAccount = new BankAccountWrapper("test account");
        dummy1BankAccount.setBankNumber(1234);

        BankAccountWrapper dummy2BankAccount = new BankAccountWrapper("test account");
        dummy2BankAccount.setId(1);
        dummy2BankAccount.setBankNumber(5678);

        Offer dummyOffer = new Offer(1, 2, dummy2BankAccount.getBankNumber(), 10f);
        dummyOffer.setOfferName("dummy offer");

        when(dummyUser.getPrincipal()).thenReturn(dummyAccount.getAccountId());
        when(accountRepository.findById(dummyAccount.getAccountId())).thenReturn(Optional.of(dummyAccount));
        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.of(dummyOffer));

        when(restCallLogic.callWithStatusCheck(GET_BANK_ACCOUNTS + dummyAccount.getAccountId(), null, BankAccountWrapper[].class, HttpMethod.GET, true))
                .thenReturn(ResponseEntity.ok(new BankAccountWrapper[]{dummy1BankAccount}));

        exception.expect(IllegalArgumentException.class);

        _logic.acceptOffer(dummyUser, dummyOffer, dummy1BankAccount.getBankNumber());
    }
}