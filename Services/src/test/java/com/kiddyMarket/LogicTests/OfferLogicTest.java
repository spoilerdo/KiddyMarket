package com.kiddyMarket.LogicTests;

import com.kiddyMarket.DataInterfaces.IAccountRepository;
import com.kiddyMarket.DataInterfaces.IOfferRepository;
import com.kiddyMarket.Entities.Account;
import com.kiddyMarket.Entities.Offer;
import com.kiddyMarket.Entities.Wrappers.ItemWrapper;
import com.kiddyMarket.Logic.Helper.RestCallLogic;
import com.kiddyMarket.Logic.OfferLogic;
import org.junit.Assert;
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

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.kiddyMarket.Logic.Constants.APIConstants.AUTH_ACCOUNTS;
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
        Offer dummyOffer = new Offer(1, 1, 10f);
        dummyOffer.setOfferName("dummy offer");
        Account dummyAccount = new Account(1, "dummyAccount");

        when(offerRepository.save(dummyOffer)).thenReturn(dummyOffer);
        when(restCallLogic.callWithStatusCheck(AUTH_ACCOUNTS, null, Account.class, HttpMethod.GET, true)).thenReturn(ResponseEntity.ok(dummyAccount));
        when(accountRepository.findById(dummyAccount.getAccountId())).thenReturn(Optional.of(dummyAccount));

        _logic.createOffer(dummyOffer);

        verify(offerRepository, times(1)).save(dummyOffer);
    }

    @Test
    public void testCreateOfferUnvalid(){
        Offer dummyOffer = new Offer(1, 0, 0f);
        dummyOffer.setOfferName("dummy offer");
        Account dummyAccount = new Account(1, "dummyAccount");

        when(restCallLogic.callWithStatusCheck(AUTH_ACCOUNTS, null, Account.class, HttpMethod.GET, true)).thenReturn(ResponseEntity.ok(dummyAccount));
        when(accountRepository.findById(dummyAccount.getAccountId())).thenReturn(Optional.of(dummyAccount));

        exception.expect(IllegalArgumentException.class);

        _logic.createOffer(dummyOffer);
    }

    @Test
    public void testDeleteOfferValid(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Object principalName = 1;
        Offer dummyOffer = new Offer(1, 1, 10f);
        dummyOffer.setOfferName("dummy offer");
        dummyOffer.setSenderId((int)principalName);

        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.of(dummyOffer));
        when(dummyUser.getPrincipal()).thenReturn(principalName);

        _logic.deleteOffer(dummyUser, dummyOffer.getOfferId());

        verify(offerRepository, times(1)).delete(dummyOffer);
    }

    @Test
    public void testDeleteOfferUnvalidAccountDoesntContainOffer(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Object principalName = 0;
        Offer dummyOffer = new Offer(1, 1, 10f);
        dummyOffer.setOfferName("dummy offer");

        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.of(dummyOffer));
        when(dummyUser.getPrincipal()).thenReturn(principalName);

        exception.expect(IllegalArgumentException.class);

        _logic.deleteOffer(dummyUser, dummyOffer.getOfferId());
    }

    @Test
    public void testDeleteOfferUnvalidOfferNotPresent(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Object principalName = 1;
        Offer dummyOffer = new Offer(1, 1, 10f);
        dummyOffer.setOfferName("dummy offer");

        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.empty());
        when(dummyUser.getPrincipal()).thenReturn(principalName);

        exception.expect(IllegalArgumentException.class);

        _logic.deleteOffer(dummyUser, dummyOffer.getOfferId());
    }

    @Test
    public void testUpdateOfferValid(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Object principalName = 1;
        Offer dummyOffer = new Offer(1, 1, 10f);
        dummyOffer.setOfferName("dummy offer");
        dummyOffer.setSenderId((int)principalName);

        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.of(dummyOffer));
        when(dummyUser.getPrincipal()).thenReturn(principalName);

        _logic.updateOffer(dummyUser, dummyOffer);

        verify(offerRepository, times(1)).save(dummyOffer);
    }

    @Test
    public void testUpdateOfferUnvalid(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Object principalName = 1;
        Offer dummyOffer = new Offer(1, 1, 10f);
        dummyOffer.setOfferName("dummy offer");

        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.empty());
        when(dummyUser.getPrincipal()).thenReturn(principalName);

        exception.expect(IllegalArgumentException.class);

        _logic.updateOffer(dummyUser, dummyOffer);
    }

    @Test
    public void testAcceptOfferValid(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Object principalName = 1;
        Offer dummyOffer = new Offer(1, 1, 10f);
        dummyOffer.setOfferName("dummy offer");

        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.of(dummyOffer));
        when(dummyUser.getPrincipal()).thenReturn(principalName);

        _logic.acceptOffer(dummyUser, dummyOffer);

        verify(offerRepository, times(1)).save(dummyOffer);
    }

    @Test
    public void testAcceptOfferUnvalid(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Offer dummyOffer = new Offer();

        exception.expect(IllegalArgumentException.class);

        _logic.acceptOffer(dummyUser, dummyOffer);
    }
}