package net.kreatious.ethereum.upgradedtelegram.erc20;

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
public class PositiveTests_Erc20 extends UpgradedtelegramApplicationTests {
    
    private final Logger log = LoggerFactory.getLogger(PositiveTests_Erc20.class);
    
    /**
     * Tests transfer by owner to Bob
     *
     * @throws Exception
     */
    @Test
    public void testTransferByOwner() throws Exception {
    
        log.info("******************** START: testTransferByOwner()");
        
        BigInteger ownerBalanceBefore = getOwnerContract().balanceOf(getOwner().getAddress()).send();
        log.info(">>>>>>>>>> Owner Supply before = " + ownerBalanceBefore.toString());
        
        BigInteger transferToBob = BigInteger.valueOf(100_000);
        BigInteger transferToBobInWei = Convert.toWei(new BigDecimal(transferToBob), Convert.Unit.ETHER).toBigInteger();
    
        log.info(">>>>>>>>>> transferToBob in Ether equivalent = " + new BigDecimal(transferToBob).toPlainString());
        log.info(">>>>>>>>>> transferToBob in Wei equivalent = " + transferToBobInWei.toString());
        
        TransactionReceipt bobTransferReceipt = getOwnerContract().transfer(getBobAddress(), transferToBobInWei).send();
    
        Token.TransferEventResponse bobTransferEventValues = getOwnerContract().getTransferEvents(bobTransferReceipt).get(0);
    
        assertThat(bobTransferEventValues._from, equalTo(getOwnerAddress()));
        assertThat(bobTransferEventValues._to, equalTo(getBobAddress()));
        assertThat(bobTransferEventValues._value, equalTo(transferToBobInWei));
    
        log.info(">>>>>>>>>> value from transfer event = " + bobTransferEventValues._value);
    
        BigInteger ownerBalanceAfter = getOwnerContract().balanceOf(getOwner().getAddress()).send();
        log.info(">>>>>>>>>> Owner Supply after = " + ownerBalanceAfter.toString());
        
        assertThat(ownerBalanceBefore, equalTo(ownerBalanceAfter.add(bobTransferEventValues._value)));
    
        log.info("******************** END: testTransferByOwner()");
    }
}
