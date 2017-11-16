package net.kreatious.ethereum.upgradedtelegram.erc20.scenario;

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

import java.math.BigInteger;

import static net.kreatious.ethereum.upgradedtelegram.contract.generated.Token.load;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApproveAllowanceAndTransferFromTest extends UpgradedtelegramApplicationTests {
    
    private final Logger log = LoggerFactory.getLogger(ApproveAllowanceAndTransferFromTest.class);
    
    /**
     * Tests the following:
     * 1. Approval of allowance limit by contract owner to Alice
     * 2. Transfer by Alice to herself on behalf of contract owner
     *
     * @throws Exception
     */
    @Test
    public void testApproveAllowanceAndTransferFrom() throws Exception {
    
        log.info("******************** START: testApproveAllowanceAndTransferFrom()");
        
        // Allowance limit, in Wei equivalent
        BigInteger allowance = BigInteger.valueOf(50_000);
        
        // Tokens to transfer, in Wei equivalent
        BigInteger transferToAlice = BigInteger.valueOf(50_000);
        
        BigInteger ownerBalance = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply before = " + ownerBalance.toString());
        
        BigInteger aliceBalance = getOwnerContract().balanceOf(getAliceAddress()).send();
        log.info(">>>>>>>>>> Alice's balance before = " + aliceBalance.toString());
        
        // Approve allowance limit for Alice
        TransactionReceipt approveReceipt = getOwnerContract().approve(getAliceAddress(), allowance).send();
        
        Token.ApprovalEventResponse approvalEventValues = getOwnerContract().getApprovalEvents(approveReceipt).get(0);
    
        log.info(">>>>>>>>>> value from approval event = " + approvalEventValues._value);
        
        // Test approval event particulars
        assertThat(approvalEventValues._owner, equalTo(getOwnerAddress()));
        assertThat(approvalEventValues._spender, equalTo(getAliceAddress()));
        assertThat(approvalEventValues._value, equalTo(allowance));
        
        // Test allowance limit for Alice
        assertThat(getOwnerContract().allowance(getOwnerAddress(), getAliceAddress()).send(), equalTo(allowance));
        
        // Test performing transfer as Alice on behalf of contract owner
        
        Credentials alice = Credentials.create(getAlicePrivateKey());
        
        // Alice requires her own contract instance
        Token aliceContract = load(getOwnerContract().getContractAddress(), getAdmin(), alice, GAS_PRICE, GAS_LIMIT);
        
        // Do transferFrom
        TransactionReceipt aliceTransferReceipt = aliceContract.transferFrom(
            getOwnerAddress(),
            getAliceAddress(),
            transferToAlice).send();
        
        Token.TransferEventResponse aliceTransferEventValues = aliceContract.getTransferEvents(aliceTransferReceipt).get(0);
    
        log.info(">>>>>>>>>> value from transfer event = " + aliceTransferEventValues._value);
    
        // Test transfer event particulars
        assertThat(aliceTransferEventValues._from, equalTo(getOwnerAddress()));
        assertThat(aliceTransferEventValues._to, equalTo(getAliceAddress()));
        assertThat(aliceTransferEventValues._value, equalTo(transferToAlice));
        
        // Test that the owner's balance has been subtracted by the tokens transferred to Alice
        ownerBalance = ownerBalance.subtract(transferToAlice);
        log.info(">>>>>>>>>> Owner's supply after = " + getOwnerContract().balanceOf(getOwnerAddress()).send().toString());
        assertThat(getOwnerContract().balanceOf(getOwnerAddress()).send(), equalTo(ownerBalance));
    
        // Test that Alice's balance has been increased by the transferred tokens
        aliceBalance = aliceBalance.add(transferToAlice);
        log.info(">>>>>>>>>> Alice's balance after = " + getOwnerContract().balanceOf(getAliceAddress()).send().toString());
        assertThat(getOwnerContract().balanceOf(getAliceAddress()).send(), equalTo(aliceBalance));
        
        log.info("******************** END: testApproveAllowanceAndTransferFrom()");
    }
}
