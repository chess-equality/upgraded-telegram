package net.kreatious.ethereum.upgradedtelegram.extensions.scenario;

import net.kreatious.ethereum.upgradedtelegram.UpgradedtelegramApplicationTests;
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

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PauseTest extends UpgradedtelegramApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(PauseTest.class);

    /**
     * Test transfer after contract has been paused
     *
     * @throws Exception
     */
    @Test
    public void testPauseThenTransfer() throws Exception {

        log.info("******************** START: testPauseThenTransfer()");

        BigInteger ownerSupply = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply before = " + ownerSupply.toString());

        BigInteger bobTokens = getOwnerContract().balanceOf(getBobAddress()).send();
        log.info(">>>>>>>>>> Bob's tokens before = " + bobTokens.toString());

        BigInteger transferToBob = BigInteger.valueOf(10_000);  // In Ether equivalent
        BigInteger transferToBobInWei = Convert.toWei(new BigDecimal(transferToBob), Convert.Unit.ETHER).toBigInteger();  // Convert to Wei equivalent

        log.info(">>>>>>>>>> transferToBob in Ether equivalent = " + transferToBob.toString());
        log.info(">>>>>>>>>> transferToBob in Wei equivalent = " + transferToBobInWei.toString());

        BigInteger currentBlockNumber = getAdmin().ethBlockNumber().sendAsync().get().getBlockNumber();
        log.info(">>>>>>>>>> current block number = " + currentBlockNumber.toString());

        // Pause contract
        TransactionReceipt setPausedReceipt = getOwnerContract().setPaused(true).send();
        log.info(">>>>>>>>>> set paused block number = " + setPausedReceipt.getBlockNumber());

        // Test that set paused succeeded
        assertThat(setPausedReceipt.getBlockNumber(), greaterThan(currentBlockNumber));

        // Do transfer
        TransactionReceipt transactionReceipt = getOwnerContract().transfer(getBobAddress(), transferToBobInWei).send();

        log.info(">>>>>>>>>> transfer block number = " + transactionReceipt.getBlockNumber());

        // Test that no transfer event was fired
        assertThat("Transfer event has been fired", 0, equalTo(getOwnerContract().getTransferEvents(transactionReceipt).size()));

        // Test that the owner's supply was not subtracted

        BigInteger ownerSupplyAfter = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply after = " + ownerSupplyAfter.toString());

        assertThat(ownerSupplyAfter, equalTo(ownerSupply));

        // Test that Bob's tokens did not increase

        BigInteger bobTokensAfter = getOwnerContract().balanceOf(getBobAddress()).send();
        log.info(">>>>>>>>>> Bob's tokens after = " + bobTokensAfter.toString());

        assertThat(bobTokensAfter, equalTo(bobTokens));

        // Un-pause contract
        TransactionReceipt unPausedReceipt = getOwnerContract().setPaused(false).send();
        log.info(">>>>>>>>>> un-paused block number = " + unPausedReceipt.getBlockNumber());

        // Test that un-paused succeeded
        assertThat(unPausedReceipt.getBlockNumber(), greaterThan(transactionReceipt.getBlockNumber()));

        log.info("******************** END: testPauseThenTransfer()");
    }
}
