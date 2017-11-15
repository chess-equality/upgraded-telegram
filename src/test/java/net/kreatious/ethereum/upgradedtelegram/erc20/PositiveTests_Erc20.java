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
public class PositiveTests_Erc20 extends UpgradedtelegramApplicationTests {
    
    private final Logger log = LoggerFactory.getLogger(PositiveTests_Erc20.class);
    
    /**
     * Tests transfer by contract owner to Bob
     *
     * @throws Exception
     */
    @Test
    public void testTransferByOwner() throws Exception {
        
        log.info("******************** START: testTransferByOwner()");
        
        BigInteger ownerBalanceBefore = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner supply before = " + ownerBalanceBefore.toString());
        
        BigInteger transferToBob = BigInteger.valueOf(100_000);
        BigInteger transferToBobInWei = Convert.toWei(new BigDecimal(transferToBob), Convert.Unit.ETHER).toBigInteger();
        
        log.info(">>>>>>>>>> transferToBob in Ether equivalent = " + new BigDecimal(transferToBob).toPlainString());
        log.info(">>>>>>>>>> transferToBob in Wei equivalent = " + transferToBobInWei.toString());
        
        TransactionReceipt transactionReceipt = getOwnerContract().transfer(getBobAddress(), transferToBobInWei).send();
        
        Token.TransferEventResponse transferEventValues = getOwnerContract().getTransferEvents(transactionReceipt).get(0);
        
        assertThat(transferEventValues._from, equalTo(getOwnerAddress()));
        assertThat(transferEventValues._to, equalTo(getBobAddress()));
        assertThat(transferEventValues._value, equalTo(transferToBobInWei));
        
        log.info(">>>>>>>>>> value from transfer event = " + transferEventValues._value);
        
        BigInteger ownerBalanceAfter = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner supply after = " + ownerBalanceAfter.toString());
        
        assertThat(ownerBalanceBefore, equalTo(ownerBalanceAfter.add(transferEventValues._value)));
        
        log.info("******************** END: testTransferByOwner()");
    }
    
    /**
     * Tests the following:
     * 1. Approval of allowance limit by contract owner to Alice
     * 2. Transfer by Alice to herself on behalf of contract owner
     *
     * @throws Exception
     */
    @Test
    public void testApproveAllowanceAndTransfer() throws Exception {
    
        // Allowance limit
        BigInteger allowance = BigInteger.valueOf(50_000);
        
        // Amount to transfer
        BigInteger transferToAlice = BigInteger.valueOf(50_000);
        
        log.info("******************** START: testApproveAllowanceAndTransfer()");
        
        BigInteger ownerBalance = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner supply before = " + ownerBalance.toString());
        
        BigInteger aliceBalance = getOwnerContract().balanceOf(getAliceAddress()).send();
        log.info(">>>>>>>>>> Alice balance before = " + aliceBalance.toString());
        
        // Approve allowance limit for Alice
        TransactionReceipt approveReceipt = getOwnerContract().approve(getAliceAddress(), allowance).send();
        
        Token.ApprovalEventResponse approvalEventValues = getOwnerContract().getApprovalEvents(approveReceipt).get(0);
        
        assertThat(approvalEventValues._owner, equalTo(getOwnerAddress()));
        assertThat(approvalEventValues._spender, equalTo(getAliceAddress()));
        assertThat(approvalEventValues._value, equalTo(allowance));
        
        log.info(">>>>>>>>>> value from transfer event = " + approvalEventValues._value);
        
        // Test allowance limit for Alice
        assertThat(getOwnerContract().allowance(getOwnerAddress(), getAliceAddress()).send(), equalTo(allowance));
        
        // Test performing transfer as Alice on behalf of contract owner
        
        Credentials alice = Credentials.create(getAlicePrivateKey());
        
        // Alice requires her own contract instance
        Token aliceContract = load(getOwnerContract().getContractAddress(), getAdmin(), alice, GAS_PRICE, GAS_LIMIT);
        
        TransactionReceipt aliceTransferReceipt = aliceContract.transferFrom(
            getOwnerAddress(),
            getAliceAddress(),
            transferToAlice).send();
        
        Token.TransferEventResponse aliceTransferEventValues = aliceContract.getTransferEvents(aliceTransferReceipt).get(0);
        
        assertThat(aliceTransferEventValues._from, equalTo(getOwnerAddress()));
        assertThat(aliceTransferEventValues._to, equalTo(getAliceAddress()));
        assertThat(aliceTransferEventValues._value, equalTo(transferToAlice));
        
        ownerBalance = ownerBalance.subtract(transferToAlice);
        aliceBalance = aliceBalance.add(transferToAlice);
        
        assertThat(getOwnerContract().balanceOf(getOwnerAddress()).send(), equalTo(ownerBalance));
        assertThat(getOwnerContract().balanceOf(getAliceAddress()).send(), equalTo(aliceBalance));
        
        log.info(">>>>>>>>>> Owner supply after = " + ownerBalance.toString());
        log.info(">>>>>>>>>> Alice balance after = " + aliceBalance.toString());
        
        log.info("******************** END: testApproveAllowanceAndTransfer()");
    }
    
    /**
     * Tests purchase of tokens by Alice
     *
     * @throws Exception
     */
    @Test
    public void testPurchase() throws Exception {
    
        log.info("******************** START: testPurchase()");
        
        BigInteger ownerBalance = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner supply before = " + ownerBalance.toString());
    
        BigInteger aliceBalance = getOwnerContract().balanceOf(getAliceAddress()).send();
        log.info(">>>>>>>>>> Alice balance before = " + aliceBalance.toString());
        
        Credentials alice = Credentials.create(getAlicePrivateKey());
    
        // Alice requires her own contract instance
        Token aliceContract = load(getOwnerContract().getContractAddress(), getAdmin(), alice, GAS_PRICE, GAS_LIMIT);
    
        BigInteger tokensPerWei = BigInteger.valueOf(2_000);
        BigInteger weiToPurchase = BigInteger.valueOf(1_000_000_000_000_000_000L);  // puchase 1 ETH worth of tokens
        
        BigInteger totalTokensToPurchase = weiToPurchase.multiply(tokensPerWei);
        log.info(">>>>>>>>>> totalTokensToPurchase = " + totalTokensToPurchase.toString());
        
        TransactionReceipt transactionReceipt = aliceContract.purchase(weiToPurchase).send();
    
        Token.TransferEventResponse transferEventResponse = getOwnerContract().getTransferEvents(transactionReceipt).get(0);
        
        assertThat(transferEventResponse._from, equalTo(getOwnerAddress()));
        assertThat(transferEventResponse._to, equalTo(getAliceAddress()));
        assertThat(transferEventResponse._value, equalTo(totalTokensToPurchase));
    
        ownerBalance = ownerBalance.subtract(totalTokensToPurchase);
        aliceBalance = aliceBalance.add(totalTokensToPurchase);
    
        assertThat(getOwnerContract().balanceOf(getOwnerAddress()).send(), equalTo(ownerBalance));
        assertThat(getOwnerContract().balanceOf(getAliceAddress()).send(), equalTo(aliceBalance));
    
        log.info(">>>>>>>>>> Owner supply after = " + ownerBalance.toString());
        log.info(">>>>>>>>>> Alice balance after = " + aliceBalance.toString());
        
        log.info("******************** END: testPurchase()");
    }
}
