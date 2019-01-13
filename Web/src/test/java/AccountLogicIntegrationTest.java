import com.kiddyMarket.App;
import com.kiddyMarket.DataInterfaces.IAccountRepository;
import com.kiddyMarket.DataInterfaces.IOfferRepository;
import com.kiddyMarket.Entities.Account;
import com.kiddyMarket.Entities.Offer;
import com.kiddyMarket.Entities.Wrappers.BankAccountWrapper;
import com.kiddyMarket.Logic.AccountLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {App.class})
@WebAppConfiguration
@Transactional
public class AccountLogicIntegrationTest {
    @Autowired
    private IOfferRepository offerRepository;

    @Autowired
    private IAccountRepository accountRepository;


    @Autowired
    private AccountLogic _logic;

    @Before
    public void setUp(){
        Account dummyAccount = new Account(1, "dummy1Account");
        BankAccountWrapper dummy1BankAccount = new BankAccountWrapper("test account");

        Offer dummy1Offer = new Offer(1, dummyAccount.getAccountId(), dummy1BankAccount.getBankNumber(), 10f);
        dummy1Offer.setOfferName("dummy 1 offer");

        Offer dummy2Offer = new Offer(2, dummyAccount.getAccountId(), dummy1BankAccount.getBankNumber(), 10f);
        dummy2Offer.setOfferName("dummy 2 offer");

        List<Offer> offers = new ArrayList<>();
        offers.add(dummy1Offer);
        offers.add(dummy2Offer);
        dummyAccount.setOffers(offers);

        offerRepository.save(dummy1Offer);
        offerRepository.save(dummy2Offer);

        accountRepository.save(dummyAccount);
    }

    @Test
    public void testGetOffersFromAccount(){
        Account dummyAccount = new Account(1, "dummy1Account");

        Assert.assertNotNull(_logic.getOffersFromAccount(dummyAccount.getAccountId()));
    }
}
