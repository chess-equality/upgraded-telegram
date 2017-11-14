package net.kreatious.ethereum.upgradedtelegram.extensions;

import net.kreatious.ethereum.upgradedtelegram.UpgradedtelegramApplicationTests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NegativeTests_Extensions extends UpgradedtelegramApplicationTests {
    
    private final Logger log = LoggerFactory.getLogger(NegativeTests_Extensions.class);
    
    /**
     * Tests a negative purchase
     *
     * @throws Exception
     */
    @Test
    public void testPurchaseNegative() throws Exception {
    
        log.info("******************** START: testPurchaseNegative()");
    
        log.info("******************** END: testPurchaseNegative()");
    }
}
