package net.kreatious.ethereum.upgradedtelegram.extensions.scenario;

import net.kreatious.ethereum.upgradedtelegram.UpgradedtelegramApplicationTests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReserveTest extends UpgradedtelegramApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(ReserveTest.class);

    /**
     * Tests setting a new number of reserved tokens
     *
     * @throws Exception
     */
    @Test
    public void testSetReserved() throws Exception {

        log.info("******************** START: testSetReserved()");

        TransactionReceipt setReservedReceipt = getOwnerContract().setReserved(BigInteger.valueOf(200_000_000)).send();  // In Ether equivalent
        log.info(">>>>>>>>>> set reserved status = " + setReservedReceipt.getStatus());

        // Test that set reserved succeeded
        assertEquals(setReservedReceipt.getStatus(), "1");

        log.info("******************** END: testSetReserved()");
    }
}
