package net.kreatious.ethereum.upgradedtelegram;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmartContractKiller extends UpgradedtelegramApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(SmartContractKiller.class);

    /**
     * ==============
     *    WARNING!
     * ==============
     *
     * Tests the kill() function which in turn calls selfdestruct().
     *
     * Only do this if you are sure at what you're doing!
     *
     * @throws Exception
     */
    @Test
    public void testKillSmartContract() throws Exception {

        log.info("******************** START: testKillSmartContract()");

        TransactionReceipt killReceipt = getOwnerContract().kill().send();
        log.info(">>>>>>>>>> kill tx hash = " + killReceipt.getTransactionHash());
        log.info(">>>>>>>>>> kill status = " + killReceipt.getStatus());

        // Test that kill succeeded
        assertEquals(killReceipt.getStatus(), "1");

        // Try to transfer
        try {

            BigInteger ownerSupply = getOwnerContract().balanceOf(getOwnerAddress()).send();
            log.info(">>>>>>>>>> Owner's supply before = " + ownerSupply.toString());

            BigInteger bobTokens = getOwnerContract().balanceOf(getBobAddress()).send();
            log.info(">>>>>>>>>> Bob's tokens before = " + bobTokens.toString());

            BigInteger transferToBob = BigInteger.valueOf(10_000);  // 10,000 tokens, in Ether equivalent
            BigInteger transferToBobInWei = Convert.toWei(new BigDecimal(transferToBob), Convert.Unit.ETHER).toBigInteger();  // Convert to Wei equivalent

            log.info(">>>>>>>>>> transferToBob in Ether equivalent = " + transferToBob.toString());
            log.info(">>>>>>>>>> transferToBob in Wei equivalent = " + transferToBobInWei.toString());

            // Do transfer
            TransactionReceipt transactionReceipt = getOwnerContract().transfer(getBobAddress(), transferToBobInWei).send();
            log.info(">>>>>>>>>> transfer tx hash = " + transactionReceipt.getTransactionHash());
            log.info(">>>>>>>>>> transfer status = " + transactionReceipt.getStatus());

            // Test that transfer has not succeeded
            assertEquals(transactionReceipt.getStatus(), "0");

            // Test that no transfer event has been fired
            assertThat("Transfer event has been fired", 0, equalTo(getOwnerContract().getTransferEvents(transactionReceipt).size()));

            // Test that the owner's supply has not been subtracted by the tokens transferred to Bob

            BigInteger ownerSupplyAfter = getOwnerContract().balanceOf(getOwnerAddress()).send();
            log.info(">>>>>>>>>> Owner's supply after = " + ownerSupplyAfter.toString());

            assertThat(ownerSupplyAfter, equalTo(ownerSupply));

            // Test that Bob's tokens have not been increased by the transferred tokens

            BigInteger bobTokensAfter = getOwnerContract().balanceOf(getBobAddress()).send();
            log.info(">>>>>>>>>> Bob's tokens after = " + bobTokensAfter.toString());

            assertThat(bobTokensAfter, equalTo(bobTokens));

        } catch (Exception e) {
            log.error("******************** EXCEPTION = " + e.getMessage());
        }

        log.info("******************** END: testKillSmartContract()");
    }
}
