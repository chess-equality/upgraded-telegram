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
public class SellTest extends UpgradedtelegramApplicationTests {
    
    private final Logger log = LoggerFactory.getLogger(SellTest.class);
    
    @Value("${tokensPerWei}")
    private String tokensPerWeiProp;
    
    /**
     * Tests selling of tokens by Alice
     *
     * @throws Exception
     */
    @Test
    public void testSell() throws Exception {
        
        log.info("******************** START: testSell()");
        
        BigInteger ownerBalance = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply before = " + ownerBalance.toString());
        
        BigInteger aliceBalance = getOwnerContract().balanceOf(getAliceAddress()).send();
        log.info(">>>>>>>>>> Alice's balance before = " + aliceBalance.toString());
        
        Credentials alice = Credentials.create(getAlicePrivateKey());
        
        // Alice requires her own contract instance
        Token aliceContract = load(getOwnerContract().getContractAddress(), getAdmin(), alice, GAS_PRICE, GAS_LIMIT);
        
        BigInteger tokensPerWei = BigInteger.valueOf(Long.parseLong(tokensPerWeiProp));
        BigInteger weiToSell = BigInteger.valueOf(1_000_000);  // In testnet, watch out for gas when sending large transactions
        
        BigInteger totalTokensToSell = weiToSell.multiply(tokensPerWei);
        log.info(">>>>>>>>>> totalTokensToSell = " + totalTokensToSell.toString());
        
        // Test first if Alice has capacity to sell the number of tokens
        assertThat("Tokens to sell is greater than Alice's balance", true, equalTo(aliceBalance.compareTo(totalTokensToSell) >= 0));
        
        // Do sell
        TransactionReceipt transactionReceipt = aliceContract.sell(totalTokensToSell).send();
        
        Token.TransferEventResponse transferEventValues = getOwnerContract().getTransferEvents(transactionReceipt).get(0);
    
        log.info(">>>>>>>>>> value from transfer event = " + transferEventValues._value);
    
        // Test transfer event particulars
        assertThat(transferEventValues._from, equalTo(getAliceAddress()));
        assertThat(transferEventValues._to, equalTo(getOwnerAddress()));
        assertThat(transferEventValues._value, equalTo(totalTokensToSell));
    
        // Only check for the balance updates in private blockchain (i.e., testrpc) since in testnet, we don't know when the transaction will be mined
        if (getActiveProfile().equals("private")) {
            
            // Test that the owner's balance has been increased by the tokens sold by Alice
            ownerBalance = ownerBalance.add(totalTokensToSell);
            log.info(">>>>>>>>>> Owner's supply after = " + getOwnerContract().balanceOf(getOwnerAddress()).send().toString());
            assertThat(getOwnerContract().balanceOf(getOwnerAddress()).send(), equalTo(ownerBalance));
    
            // Test that Alice's balance has been subtracted by the sold tokens
            aliceBalance = aliceBalance.subtract(totalTokensToSell);
            log.info(">>>>>>>>>> Alice's balance after = " + getOwnerContract().balanceOf(getAliceAddress()).send().toString());
            assertThat(getOwnerContract().balanceOf(getAliceAddress()).send(), equalTo(aliceBalance));
        }
        
        log.info("******************** END: testSell()");
    }
}
