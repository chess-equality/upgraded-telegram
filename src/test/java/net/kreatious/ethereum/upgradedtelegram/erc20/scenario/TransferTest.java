package net.kreatious.ethereum.upgradedtelegram.erc20.scenario;

import net.kreatious.ethereum.upgradedtelegram.UpgradedtelegramApplicationTests;
import net.kreatious.ethereum.upgradedtelegram.contract.generated.Token;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.profiles.active}")
    private String activeProfile;

    /**
     * Tests transfer by contract owner to Bob
     *
     * @throws Exception
     */
    @Test
    public void testTransferByOwner() throws Exception {
        
        log.info("******************** START: testTransferByOwner()");
        
        BigInteger ownerBalance = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply before = " + ownerBalance.toString());
    
        BigInteger bobBalance = getOwnerContract().balanceOf(getBobAddress()).send();
        log.info(">>>>>>>>>> Bob's balance before = " + bobBalance.toString());
        
        BigInteger transferToBob = BigInteger.valueOf(10_000);  // In Ether equivalent
        BigInteger transferToBobInWei = Convert.toWei(new BigDecimal(transferToBob), Convert.Unit.ETHER).toBigInteger();  // Convert to Wei equivalent
        
        log.info(">>>>>>>>>> transferToBob in Ether equivalent = " + transferToBob.toString());
        log.info(">>>>>>>>>> transferToBob in Wei equivalent = " + transferToBobInWei.toString());

        CountDownLatch transferEventCountDownLatch = new CountDownLatch(2);
        Subscription transferEventSubscription = getOwnerContract().transferEventObservable(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST).subscribe(
                        transferEventResponse -> transferEventCountDownLatch.countDown()
        );

        CountDownLatch approvalEventCountDownLatch = new CountDownLatch(1);
        Subscription approvalEventSubscription = getOwnerContract().approvalEventObservable(
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
        approvalEventCountDownLatch.await(DEFAULT_POLLING_FREQUENCY, TimeUnit.MILLISECONDS);

        approvalEventSubscription.unsubscribe();
        transferEventSubscription.unsubscribe();

        Thread.sleep(1000);

        assertTrue(approvalEventSubscription.isUnsubscribed());
        assertTrue(transferEventSubscription.isUnsubscribed());

        // Only check for the balance updates in private blockchain (i.e., testrpc) since in testnet, we don't know when the transaction will be mined
        if (activeProfile.equals("private")) {

            // Test that the owner's balance has been subtracted by the tokens transferred to Bob
            ownerBalance = ownerBalance.subtract(transferToBobInWei);
            log.info(">>>>>>>>>> Owner's supply after = " + getOwnerContract().balanceOf(getOwnerAddress()).send().toString());
            assertThat(getOwnerContract().balanceOf(getOwnerAddress()).send(), equalTo(ownerBalance));

            // Test that Bob's balance has been increased by the transferred tokens
            bobBalance = bobBalance.add(transferToBobInWei);
            log.info(">>>>>>>>>> Bob's balance after = " + getOwnerContract().balanceOf(getBobAddress()).send().toString());
            assertThat(getOwnerContract().balanceOf(getBobAddress()).send(), equalTo(bobBalance));
        }

        log.info("******************** END: testTransferByOwner()");
    }
}
