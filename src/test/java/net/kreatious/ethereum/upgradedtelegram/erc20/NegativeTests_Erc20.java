package net.kreatious.ethereum.upgradedtelegram.erc20;

import net.kreatious.ethereum.upgradedtelegram.UpgradedtelegramApplicationTests;
import net.kreatious.ethereum.upgradedtelegram.contract.generated.Token;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

import static net.kreatious.ethereum.upgradedtelegram.contract.generated.Token.load;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NegativeTests_Erc20 extends UpgradedtelegramApplicationTests {
    
    private final Logger log = LoggerFactory.getLogger(NegativeTests_Erc20.class);
    
    /**
     * Tests transfer by Bob to himself even if he is not owner of the contract
     *
     * @throws Exception
     */
    @Test
    public void testTransferByNonOwner() throws Exception {
    
        log.info("******************** START: testTransferByNonOwner()");
        
        BigInteger ownerBalanceBefore = getOwnerContract().balanceOf(getOwner().getAddress()).send();
        log.info(">>>>>>>>>> Owner Supply before = " + ownerBalanceBefore.toString());
        
        Credentials bob = Credentials.create(getBobPrivateKey());
        
        Token bobContract = load(getContractAddress(), getAdmin(), bob, GAS_PRICE, GAS_LIMIT);
        BigInteger bobBalanceBefore = getOwnerContract().balanceOf(getBobAddress()).send();
        log.info(">>>>>>>>>> bobBalanceBefore = " + bobBalanceBefore);
    
        BigInteger transferToBob = BigInteger.valueOf(100_000);
        BigInteger transferToBobInWei = Convert.toWei(new BigDecimal(transferToBob), Convert.Unit.ETHER).toBigInteger();
    
        log.info(">>>>>>>>>> transferToBob in Ether equivalent = " + new BigDecimal(transferToBob).toPlainString());
        log.info(">>>>>>>>>> transferToBob in Wei equivalent = " + transferToBobInWei.toString());
    
        // Transfer to Bob by Bob
        TransactionReceipt bobTransferReceipt = bobContract.transfer(getBobAddress(), transferToBobInWei).send();
        
        Token.TransferEventResponse bobTransferEventValues = bobContract.getTransferEvents(bobTransferReceipt).get(0);
        
        assertThat(bobTransferEventValues._from, equalTo(getBobAddress()));
        assertThat(bobTransferEventValues._to, equalTo(getBobAddress()));
        assertThat(bobTransferEventValues._value, equalTo(transferToBobInWei));
    
        log.info(">>>>>>>>>> value from transfer event = " + bobTransferEventValues._value);
    
        BigInteger bobBalanceAfter = getOwnerContract().balanceOf(getBobAddress()).send();
        log.info(">>>>>>>>>> bobBalanceAfter = " + bobBalanceAfter);
        
        assertThat(bobBalanceBefore, equalTo(bobBalanceAfter));
    
        BigInteger ownerBalanceAfter = getOwnerContract().balanceOf(getOwner().getAddress()).send();
        log.info(">>>>>>>>>> Owner Supply after = " + ownerBalanceAfter.toString());
    
        assertThat(ownerBalanceBefore, equalTo(ownerBalanceAfter));
    
        log.info("******************** END: testTransferByNonOwner()");
    }
}
