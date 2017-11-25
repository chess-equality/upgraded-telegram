package net.kreatious.ethereum.upgradedtelegram.erc20.scenario;

import net.kreatious.ethereum.upgradedtelegram.UpgradedtelegramApplicationTests;
import net.kreatious.ethereum.upgradedtelegram.contract.generated.Token;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;
import rx.Subscription;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.web3j.tx.TransactionManager.DEFAULT_POLLING_FREQUENCY;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransferTest extends UpgradedtelegramApplicationTests {
    
    private final Logger log = LoggerFactory.getLogger(TransferTest.class);

    /**
     * Tests transfer by contract owner to Bob
     *
     * @throws Exception
     */
    @Test
    public void testTransferByOwner() throws Exception {
        
        log.info("******************** START: testTransferByOwner()");
        
        BigInteger ownerSupply = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply before = " + ownerSupply.toString());
    
        BigInteger bobTokens = getOwnerContract().balanceOf(getBobAddress()).send();
        log.info(">>>>>>>>>> Bob's tokens before = " + bobTokens.toString());

        BigInteger transferToBob = BigInteger.valueOf(10_000);  // In Ether equivalent
        BigInteger transferToBobInWei = Convert.toWei(new BigDecimal(transferToBob), Convert.Unit.ETHER).toBigInteger();  // Convert to Wei equivalent
        
        log.info(">>>>>>>>>> transferToBob in Ether equivalent = " + transferToBob.toString());
        log.info(">>>>>>>>>> transferToBob in Wei equivalent = " + transferToBobInWei.toString());

        CountDownLatch transferEventCountDownLatch = new CountDownLatch(1);
        Subscription transferEventSubscription = getOwnerContract().transferEventObservable(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST).subscribe(
                        transferEventResponse -> transferEventCountDownLatch.countDown()
        );

        // Do transfer
        TransactionReceipt transactionReceipt = getOwnerContract().transfer(getBobAddress(), transferToBobInWei).send();

        Token.TransferEventResponse transferEventValues = getOwnerContract().getTransferEvents(transactionReceipt).get(0);
    
        log.info(">>>>>>>>>> value from transfer event = " + transferEventValues._value);
        
        // Test transfer event particulars
        assertThat(transferEventValues._from, equalTo(getOwnerAddress()));
        assertThat(transferEventValues._to, equalTo(getBobAddress()));
        assertThat(transferEventValues._value, equalTo(transferToBobInWei));

        transferEventCountDownLatch.await(DEFAULT_POLLING_FREQUENCY, TimeUnit.MILLISECONDS);
        transferEventSubscription.unsubscribe();
        Thread.sleep(10000);

        assertTrue(transferEventSubscription.isUnsubscribed());

        // Test that the owner's supply has been subtracted by the tokens transferred to Bob

        ownerSupply = ownerSupply.subtract(transferToBobInWei);

        BigInteger ownerSupplyAfter = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply after = " + ownerSupplyAfter.toString());

        assertThat(ownerSupplyAfter, equalTo(ownerSupply));

        // Test that Bob's tokens has been increased by the transferred tokens

        bobTokens = bobTokens.add(transferToBobInWei);

        BigInteger bobTokensAfter = getOwnerContract().balanceOf(getBobAddress()).send();
        log.info(">>>>>>>>>> Bob's tokens after = " + bobTokensAfter.toString());

        assertThat(bobTokensAfter, equalTo(bobTokens));

        log.info("******************** END: testTransferByOwner()");
    }
}
