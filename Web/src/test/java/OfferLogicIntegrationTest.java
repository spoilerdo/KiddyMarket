import com.kiddyMarket.App;
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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import static com.kiddyMarket.Logic.Constants.APIConstants.GET_BANK_ACCOUNTS;
import static com.kiddyMarket.Logic.Constants.APIConstants.INVENTORY_ITEM_GET;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {App.class})
@WebAppConfiguration
@Transactional
public class OfferLogicIntegrationTest {
    @Autowired
    private IOfferRepository offerRepository;

    @Autowired
    private IAccountRepository accountRepository;

    @Mock
    private RestCallLogic restCallLogic;

    @Autowired
    @InjectMocks
    private OfferLogic _logic;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        Account dummyAccount = new Account(1, "dummyAccount");
        BankAccountWrapper dummy1BankAccount = new BankAccountWrapper("test account");

        Offer dummy1Offer = new Offer(1, dummyAccount.getAccountId(), dummy1BankAccount.getBankNumber(), 10f);
        dummy1Offer.setOfferName("dummy 1 offer");

        Offer dummy2Offer = new Offer(2, dummyAccount.getAccountId(), dummy1BankAccount.getBankNumber(), 10f);
        dummy2Offer.setOfferName("dummy 2 offer");

        offerRepository.save(dummy1Offer);
        offerRepository.save(dummy2Offer);

        accountRepository.save(dummyAccount);
    }

    @Test
    public void testCreateOffer(){
        Authentication dummyUser = Mockito.mock(Authentication.class);
        Account dummyAccount = new Account(1, "dummyAccount");

        BankAccountWrapper dummy1BankAccount = new BankAccountWrapper("test account");
        dummy1BankAccount.setBankNumber(1234);

        Offer dummyOffer = new Offer(3, dummyAccount.getAccountId(), dummy1BankAccount.getBankNumber(), 10f);
        dummyOffer.setOfferName("dummy offer");

        when(dummyUser.getPrincipal()).thenReturn(dummyAccount.getAccountId());
        when(restCallLogic.callWithStatusCheck(GET_BANK_ACCOUNTS + dummyAccount.getAccountId(), null, BankAccountWrapper[].class, HttpMethod.GET, true))
                .thenReturn(ResponseEntity.ok(new BankAccountWrapper[]{dummy1BankAccount}));
        when(restCallLogic.callWithStatusCheck(INVENTORY_ITEM_GET + dummyOffer.getItemId(), null, ItemWrapper.class, HttpMethod.GET, false))
                .thenReturn(ResponseEntity.ok(new ItemWrapper("test item", "d", Condition.FN, Quality.Ancient, 1f)));

        _logic.createOffer(dummyUser, dummyOffer);

        Assert.assertTrue(offerRepository.findById(dummyOffer.getOfferId()).isPresent());
    }
}
