package net.kreatious.ethereum.upgradedtelegram.extensions.scenario;

import net.kreatious.ethereum.upgradedtelegram.UpgradedtelegramApplicationTests;
import net.kreatious.ethereum.upgradedtelegram.contract.generated.Token;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import rx.Subscription;

import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static net.kreatious.ethereum.upgradedtelegram.contract.generated.Token.load;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.web3j.tx.TransactionManager.DEFAULT_POLLING_FREQUENCY;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)  // Purchase first then sell
public class PurchaseAndSellTest extends UpgradedtelegramApplicationTests {
    
    private final Logger log = LoggerFactory.getLogger(PurchaseAndSellTest.class);
    
    @Value("${tokensPerWei}")
    private String tokensPerWeiProp;
    
    /**
     * Tests purchase of tokens by Alice
     *
     * @throws Exception
     */
    @Test
    public void testPurchase() throws Exception {
        
        log.info("******************** START: testPurchase()");
        
        BigInteger ownerSupply = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply before = " + ownerSupply.toString());
        
        BigInteger aliceTokens = getOwnerContract().balanceOf(getAliceAddress()).send();
        log.info(">>>>>>>>>> Alice's tokens before = " + aliceTokens.toString());
        
        Credentials alice = Credentials.create(getAlicePrivateKey());
        
        // Alice requires her own contract instance
        Token aliceContract = load(getOwnerContract().getContractAddress(), getAdmin(), alice, GAS_PRICE, GAS_LIMIT);
        
        BigInteger tokensPerWei = BigInteger.valueOf(Long.parseLong(tokensPerWeiProp));
        BigInteger weiToPurchase = BigInteger.valueOf(1_000_000);  // In testnet, watch out if Alice has Ether to purchase and pay gas
        
        BigInteger totalTokensToPurchase = weiToPurchase.multiply(tokensPerWei);
        log.info(">>>>>>>>>> totalTokensToPurchase = " + totalTokensToPurchase.toString());

        // Test first if Alice has sufficient Ether to purchase the number of tokens

        EthGetBalance ethGetBalance = getAdmin().ethGetBalance(getAliceAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        log.info(">>>>>>>>>> Alice's Ether balance in Wei = " + ethGetBalance.getBalance().toString());
        log.info(">>>>>>>>>> Tokens to purchase in Wei = " + weiToPurchase.toString());

        assertThat("Alice has insufficient Ether to purchase the tokens", true, equalTo(ethGetBalance.getBalance().compareTo(weiToPurchase) >= 0));

        CountDownLatch transferEventCountDownLatch = new CountDownLatch(1);
        Subscription transferEventSubscription = aliceContract.transferEventObservable(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST).subscribe(
                transferEventResponse -> transferEventCountDownLatch.countDown()
        );

        // Do purchase
        TransactionReceipt transactionReceipt = aliceContract.purchase(weiToPurchase).send();
        
        Token.TransferEventResponse transferEventValues = getOwnerContract().getTransferEvents(transactionReceipt).get(0);
    
        log.info(">>>>>>>>>> value from transfer event = " + transferEventValues._value);
        
        // Test transfer event particulars
        assertThat(transferEventValues._from, equalTo(getOwnerAddress()));
        assertThat(transferEventValues._to, equalTo(getAliceAddress()));
        assertThat(transferEventValues._value, equalTo(totalTokensToPurchase));

        transferEventCountDownLatch.await(DEFAULT_POLLING_FREQUENCY, TimeUnit.MILLISECONDS);
        if (!getActiveProfile().equals("private")) {
            transferEventSubscription.unsubscribe();
        }
        Thread.sleep(1000);
        if (!getActiveProfile().equals("private")) {
            assertTrue(transferEventSubscription.isUnsubscribed());
        }
        
        // Test that the owner's supply has been subtracted by the tokens purchased by Alice
        
        ownerSupply = ownerSupply.subtract(totalTokensToPurchase);

        BigInteger ownerSupplyAfter = aliceContract.balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply after = " + ownerSupplyAfter.toString());
        
        assertThat(ownerSupplyAfter, equalTo(ownerSupply));

        // Test that Alice's tokens have been increased by the purchased tokens
        
        aliceTokens = aliceTokens.add(totalTokensToPurchase);
        
        BigInteger aliceTokensAfter = aliceContract.balanceOf(getAliceAddress()).send();
        log.info(">>>>>>>>>> Alice's tokens after = " + aliceTokensAfter.toString());
        
        assertThat(aliceTokensAfter, equalTo(aliceTokens));
        
        log.info("******************** END: testPurchase()");
    }

    /**
     * Tests selling of tokens by Alice
     *
     * @throws Exception
     */
    @Test
    public void testSell() throws Exception {

        log.info("******************** START: testSell()");

        BigInteger ownerSupply = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply before = " + ownerSupply.toString());

        BigInteger aliceTokens = getOwnerContract().balanceOf(getAliceAddress()).send();
        log.info(">>>>>>>>>> Alice's tokens before = " + aliceTokens.toString());

        Credentials alice = Credentials.create(getAlicePrivateKey());

        // Alice requires her own contract instance
        Token aliceContract = load(getOwnerContract().getContractAddress(), getAdmin(), alice, GAS_PRICE, GAS_LIMIT);

        BigInteger tokensPerWei = BigInteger.valueOf(Long.parseLong(tokensPerWeiProp));
        BigInteger weiToSell = BigInteger.valueOf(1_000_000);

        BigInteger totalTokensToSell = weiToSell.multiply(tokensPerWei);
        log.info(">>>>>>>>>> totalTokensToSell = " + totalTokensToSell.toString());

        // Test first if Alice has capacity to sell the number of tokens
        assertThat("Tokens to sell is greater than Alice's balance", true, equalTo(aliceTokens.compareTo(totalTokensToSell) >= 0));

        CountDownLatch transferEventCountDownLatch = new CountDownLatch(1);
        Subscription transferEventSubscription = aliceContract.transferEventObservable(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST).subscribe(
                transferEventResponse -> transferEventCountDownLatch.countDown()
        );

        // Do sell
        TransactionReceipt transactionReceipt = aliceContract.sell(totalTokensToSell).send();

        Token.TransferEventResponse transferEventValues = getOwnerContract().getTransferEvents(transactionReceipt).get(0);

        log.info(">>>>>>>>>> value from transfer event = " + transferEventValues._value);

        // Test transfer event particulars
        assertThat(transferEventValues._from, equalTo(getAliceAddress()));
        assertThat(transferEventValues._to, equalTo(getOwnerAddress()));
        assertThat(transferEventValues._value, equalTo(totalTokensToSell));

        transferEventCountDownLatch.await(DEFAULT_POLLING_FREQUENCY, TimeUnit.MILLISECONDS);
        if (!getActiveProfile().equals("private")) {
            transferEventSubscription.unsubscribe();
        }
        Thread.sleep(1000);
        if (!getActiveProfile().equals("private")) {
            assertTrue(transferEventSubscription.isUnsubscribed());
        }

        // Test that the owner's supply has been increased by the tokens sold by Alice

        ownerSupply = ownerSupply.add(totalTokensToSell);

        BigInteger ownerSupplyAfter = aliceContract.balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply after = " + ownerSupplyAfter.toString());

        assertThat(ownerSupplyAfter, equalTo(ownerSupply));

        // Test that Alice's tokens have been subtracted by the sold tokens

        aliceTokens = aliceTokens.subtract(totalTokensToSell);

        BigInteger aliceTokensAfter = aliceContract.balanceOf(getAliceAddress()).send();
        log.info(">>>>>>>>>> Alice's tokens after = " + aliceTokensAfter.toString());

        assertThat(aliceTokensAfter, equalTo(aliceTokens));

        log.info("******************** END: testSell()");
    }
}
