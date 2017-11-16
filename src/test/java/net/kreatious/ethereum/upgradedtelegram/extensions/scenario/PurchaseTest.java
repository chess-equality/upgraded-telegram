package net.kreatious.ethereum.upgradedtelegram.extensions.scenario;

import net.kreatious.ethereum.upgradedtelegram.UpgradedtelegramApplicationTests;
import net.kreatious.ethereum.upgradedtelegram.contract.generated.Token;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

import static net.kreatious.ethereum.upgradedtelegram.contract.generated.Token.load;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PurchaseTest extends UpgradedtelegramApplicationTests {
    
    private final Logger log = LoggerFactory.getLogger(PurchaseTest.class);
    
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
        
        BigInteger ownerBalance = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply before = " + ownerBalance.toString());
        
        BigInteger aliceBalance = getOwnerContract().balanceOf(getAliceAddress()).send();
        log.info(">>>>>>>>>> Alice's balance before = " + aliceBalance.toString());
        
        Credentials alice = Credentials.create(getAlicePrivateKey());
        
        // Alice requires her own contract instance
        Token aliceContract = load(getOwnerContract().getContractAddress(), getAdmin(), alice, GAS_PRICE, GAS_LIMIT);
        
        BigInteger tokensPerWei = BigInteger.valueOf(Long.parseLong(tokensPerWeiProp));
        BigInteger weiToPurchase = BigInteger.valueOf(1_000_000_000_000_000_000L);  // purchase 1 ETH worth of tokens
        
        BigInteger totalTokensToPurchase = weiToPurchase.multiply(tokensPerWei);
        log.info(">>>>>>>>>> totalTokensToPurchase = " + totalTokensToPurchase.toString());
        
        // Do purchase
        TransactionReceipt transactionReceipt = aliceContract.purchase(weiToPurchase).send();
        
        Token.TransferEventResponse transferEventValues = getOwnerContract().getTransferEvents(transactionReceipt).get(0);
    
        log.info(">>>>>>>>>> value from transfer event = " + transferEventValues._value);
        
        // Test transfer event particulars
        assertThat(transferEventValues._from, equalTo(getOwnerAddress()));
        assertThat(transferEventValues._to, equalTo(getAliceAddress()));
        assertThat(transferEventValues._value, equalTo(totalTokensToPurchase));
    
        // Test that the owner's balance has been subtracted by the tokens purchased by Alice
        ownerBalance = ownerBalance.subtract(totalTokensToPurchase);
        log.info(">>>>>>>>>> Owner's supply after = " + getOwnerContract().balanceOf(getOwnerAddress()).send().toString());
        assertThat(getOwnerContract().balanceOf(getOwnerAddress()).send(), equalTo(ownerBalance));
    
        // Test that Alice's balance has been increased by the purchased tokens
        aliceBalance = aliceBalance.add(totalTokensToPurchase);
        log.info(">>>>>>>>>> Alice's balance after = " + getOwnerContract().balanceOf(getAliceAddress()).send().toString());
        assertThat(getOwnerContract().balanceOf(getAliceAddress()).send(), equalTo(aliceBalance));
        
        log.info("******************** END: testPurchase()");
    }
}
