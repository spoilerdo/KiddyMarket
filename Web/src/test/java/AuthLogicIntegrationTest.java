import com.kiddyMarket.App;
import com.kiddyMarket.DataInterfaces.IAccountRepository;
import com.kiddyMarket.Entities.Account;
import com.kiddyMarket.Logic.AuthLogic;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {App.class})
@WebAppConfiguration
@Transactional
public class AuthLogicIntegrationTest {
    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private AuthLogic _logic;

    @Test
    public void LoadUserByUsernameTest(){
        Account dummyAccount = new Account(1, "dummy");

        _logic.loadUserById(dummyAccount.getAccountId(), dummyAccount.getUsername());

        Assert.assertNotNull(accountRepository.findByUsername(dummyAccount.getUsername()));
        Assert.assertTrue(accountRepository.findByUsername(dummyAccount.getUsername()).isPresent());
    }
}
