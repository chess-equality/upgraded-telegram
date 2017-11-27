package net.kreatious.ethereum.upgradedtelegram.erc20.scenario;

import net.kreatious.ethereum.upgradedtelegram.UpgradedtelegramApplicationTests;
import net.kreatious.ethereum.upgradedtelegram.contract.generated.Token;
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
import static org.junit.Assert.assertThat;

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
        
        // Do transfer
        TransactionReceipt transactionReceipt = getOwnerContract().transfer(getBobAddress(), transferToBobInWei).send();

        Token.TransferEventResponse transferEventValues = getOwnerContract().getTransferEvents(transactionReceipt).get(0);
    
        log.info(">>>>>>>>>> value from transfer event = " + transferEventValues._value);
        
        // Test transfer event particulars
        assertThat(transferEventValues._from, equalTo(getOwnerAddress()));
        assertThat(transferEventValues._to, equalTo(getBobAddress()));
        assertThat(transferEventValues._value, equalTo(transferToBobInWei));

        // Test that the owner's supply has been subtracted by the tokens transferred to Bob

        ownerSupply = ownerSupply.subtract(transferToBobInWei);

        BigInteger ownerSupplyAfter = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply after = " + ownerSupplyAfter.toString());

        assertThat(ownerSupplyAfter, equalTo(ownerSupply));

        // Test that Bob's tokens have been increased by the transferred tokens

        bobTokens = bobTokens.add(transferToBobInWei);

        BigInteger bobTokensAfter = getOwnerContract().balanceOf(getBobAddress()).send();
        log.info(">>>>>>>>>> Bob's tokens after = " + bobTokensAfter.toString());

        assertThat(bobTokensAfter, equalTo(bobTokens));

        log.info("******************** END: testTransferByOwner()");
    }
}
