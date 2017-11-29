package net.kreatious.ethereum.upgradedtelegram.erc20.scenario;

import net.kreatious.ethereum.upgradedtelegram.UpgradedtelegramApplicationTests;
import net.kreatious.ethereum.upgradedtelegram.contract.generated.Token;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransferTest extends UpgradedtelegramApplicationTests {
    
    private final Logger log = LoggerFactory.getLogger(TransferTest.class);

    /**
     * Tests transfer by contract owner to Bob
     *
     * @throws Exception
     */
    @Test
    public void test1TransferByOwner() throws Exception {
        
        log.info("******************** START: test1TransferByOwner()");
        
        BigInteger ownerSupply = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply before = " + ownerSupply.toString());
    
        BigInteger bobTokens = getOwnerContract().balanceOf(getBobAddress()).send();
        log.info(">>>>>>>>>> Bob's tokens before = " + bobTokens.toString());

        BigInteger transferToBob = BigInteger.valueOf(10_000);  // 10,000 tokens, in Ether equivalent
        BigInteger transferToBobInWei = Convert.toWei(new BigDecimal(transferToBob), Convert.Unit.ETHER).toBigInteger();  // Convert to Wei equivalent
        
        log.info(">>>>>>>>>> transferToBob in Ether equivalent = " + transferToBob.toString());
        log.info(">>>>>>>>>> transferToBob in Wei equivalent = " + transferToBobInWei.toString());
        
        // Do transfer by contract owner
        TransactionReceipt transactionReceipt = getOwnerContract().transfer(getBobAddress(), transferToBobInWei).send();
        log.info(">>>>>>>>>> transfer tx hash = " + transactionReceipt.getTransactionHash());
        log.info(">>>>>>>>>> transfer status = " + transactionReceipt.getStatus());

        // Test that transfer has succeeded
        assertEquals(transactionReceipt.getStatus(), "1");

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

        log.info("******************** END: test1TransferByOwner()");
    }

    /**
     * Tests transfer by Bob to Alice
     *
     * @throws Exception
     */
    @Test
    public void test2TransferByNonOwner() throws Exception {

        log.info("******************** START: test2TransferByNonOwner()");

        BigInteger ownerSupply = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply before = " + ownerSupply.toString());

        BigInteger bobTokens = getOwnerContract().balanceOf(getBobAddress()).send();
        log.info(">>>>>>>>>> Bob's tokens before = " + bobTokens.toString());

        BigInteger aliceTokens = getOwnerContract().balanceOf(getAliceAddress()).send();
        log.info(">>>>>>>>>> Alice's tokens before = " + aliceTokens.toString());

        BigInteger transferToAlice = BigInteger.valueOf(1_000);  // 1,000 tokens, in Ether equivalent
        BigInteger transferToAliceInWei = Convert.toWei(new BigDecimal(transferToAlice), Convert.Unit.ETHER).toBigInteger();  // Convert to Wei equivalent

        log.info(">>>>>>>>>> transferToAlice in Ether equivalent = " + transferToAlice.toString());
        log.info(">>>>>>>>>> transferToAlice in Wei equivalent = " + transferToAliceInWei.toString());

        // Bob requires his own contract instance
        Credentials bob = Credentials.create(getBobPrivateKey());
        Token bobContract = load(getContractAddress(), getAdmin(), bob, getGasPrice(), getGasLimit());

        // Do transfer by Bob to Alice
        TransactionReceipt transactionReceipt = bobContract.transfer(getAliceAddress(), transferToAliceInWei).send();
        log.info(">>>>>>>>>> transfer tx hash = " + transactionReceipt.getTransactionHash());
        log.info(">>>>>>>>>> transfer status = " + transactionReceipt.getStatus());

        // Test that transfer has succeeded
        assertEquals(transactionReceipt.getStatus(), "1");

        Token.TransferEventResponse transferEventValues = bobContract.getTransferEvents(transactionReceipt).get(0);
        log.info(">>>>>>>>>> value from transfer event = " + transferEventValues._value);

        // Test transfer event particulars
        assertThat(transferEventValues._from, equalTo(getBobAddress()));
        assertThat(transferEventValues._to, equalTo(getAliceAddress()));
        assertThat(transferEventValues._value, equalTo(transferToAliceInWei));

        // Test that the owner's supply has not been subtracted by the tokens transferred by Bob

        BigInteger ownerSupplyAfter = bobContract.balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply after = " + ownerSupplyAfter.toString());

        assertThat(ownerSupplyAfter, equalTo(ownerSupply));

        // Test that Bob's tokens have been subtracted by the transferred tokens to Alice

        bobTokens = bobTokens.subtract(transferToAliceInWei);

        BigInteger bobTokensAfter = bobContract.balanceOf(getBobAddress()).send();
        log.info(">>>>>>>>>> Bob's tokens after = " + bobTokensAfter.toString());

        assertThat(bobTokensAfter, equalTo(bobTokens));

        // Test that Alice's tokens have been increased by the transferred tokens

        aliceTokens = aliceTokens.add(transferToAliceInWei);

        BigInteger aliceTokensAfter = bobContract.balanceOf(getAliceAddress()).send();
        log.info(">>>>>>>>>> Alice's tokens after = " + aliceTokensAfter.toString());

        assertThat(aliceTokensAfter, equalTo(aliceTokens));

        log.info("******************** END: test2TransferByNonOwner()");
    }

    /**
     * Tests transfer by Bob to himself
     *
     * @throws Exception
     */
    @Test
    public void test3TransferByNonOwnerToHimself() throws Exception {

        log.info("******************** START: test3TransferByNonOwnerToHimself()");

        BigInteger ownerSupply = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply before = " + ownerSupply.toString());

        BigInteger bobTokens = getOwnerContract().balanceOf(getBobAddress()).send();
        log.info(">>>>>>>>>> Bob's tokens before = " + bobTokens.toString());

        BigInteger transferToHimself = BigInteger.valueOf(1_000);  // 1,000 tokens, in Ether equivalent
        BigInteger transferToHimselfInWei = Convert.toWei(new BigDecimal(transferToHimself), Convert.Unit.ETHER).toBigInteger();  // Convert to Wei equivalent

        log.info(">>>>>>>>>> transferToHimself in Ether equivalent = " + transferToHimself.toString());
        log.info(">>>>>>>>>> transferToHimselfInWei in Wei equivalent = " + transferToHimselfInWei.toString());

        // Bob requires his own contract instance
        Credentials bob = Credentials.create(getBobPrivateKey());
        Token bobContract = load(getContractAddress(), getAdmin(), bob, getGasPrice(), getGasLimit());

        // Do transfer by Bob to himself
        TransactionReceipt transactionReceipt = bobContract.transfer(getBobAddress(), transferToHimselfInWei).send();
        log.info(">>>>>>>>>> transfer tx hash = " + transactionReceipt.getTransactionHash());
        log.info(">>>>>>>>>> transfer status = " + transactionReceipt.getStatus());

        // Test that transfer has succeeded
        assertEquals(transactionReceipt.getStatus(), "1");

        Token.TransferEventResponse transferEventValues = bobContract.getTransferEvents(transactionReceipt).get(0);
        log.info(">>>>>>>>>> value from transfer event = " + transferEventValues._value);

        // Test transfer event particulars
        assertThat(transferEventValues._from, equalTo(getBobAddress()));
        assertThat(transferEventValues._to, equalTo(getBobAddress()));
        assertThat(transferEventValues._value, equalTo(transferToHimselfInWei));

        // Test that the owner's supply has not been subtracted by the tokens transferred by Bob to himself

        BigInteger ownerSupplyAfter = bobContract.balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply after = " + ownerSupplyAfter.toString());

        assertThat(ownerSupplyAfter, equalTo(ownerSupply));

        // Test that Bob's tokens have also stayed the same

        BigInteger bobTokensAfter = bobContract.balanceOf(getBobAddress()).send();
        log.info(">>>>>>>>>> Bob's tokens after = " + bobTokensAfter.toString());

        assertThat(bobTokensAfter, equalTo(bobTokens));

        log.info("******************** END: test3TransferByNonOwnerToHimself()");
    }

    /**
     * Tests transfer by John to himself but who has not bought any tokens
     *
     * @throws Exception
     */
    @Test
    public void test4TransferByNonBuyer() throws Exception {

        log.info("******************** START: test4TransferByNonBuyer()");

        BigInteger ownerSupply = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply before = " + ownerSupply.toString());

        BigInteger johnTokens = getOwnerContract().balanceOf(getJohnAddress()).send();
        log.info(">>>>>>>>>> John's tokens before = " + johnTokens.toString());

        BigInteger transferToJohn = BigInteger.valueOf(1_000);  // 1,000 tokens, in Ether equivalent
        BigInteger transferToJohnInWei = Convert.toWei(new BigDecimal(transferToJohn), Convert.Unit.ETHER).toBigInteger();  // Convert to Wei equivalent

        log.info(">>>>>>>>>> transferToJohn in Ether equivalent = " + transferToJohn.toString());
        log.info(">>>>>>>>>> transferToJohnInWei in Wei equivalent = " + transferToJohnInWei.toString());

        // John requires his own contract instance
        Credentials john = Credentials.create(getJohnPrivateKey());
        Token johnContract = load(getContractAddress(), getAdmin(), john, getGasPrice(), getGasLimit());

        // Do transfer by John to himself
        // try - catch is for testrpc
        try {

            TransactionReceipt transactionReceipt = johnContract.transfer(getJohnAddress(), transferToJohnInWei).send();
            log.info(">>>>>>>>>> transfer tx hash = " + transactionReceipt.getTransactionHash());
            log.info(">>>>>>>>>> transfer status = " + transactionReceipt.getStatus());

            // Test that transfer has not succeeded
            assertEquals(transactionReceipt.getStatus(), "0");

            // Test that no transfer event has been fired
            assertThat("Transfer event has been fired", 0, equalTo(johnContract.getTransferEvents(transactionReceipt).size()));

        } catch (Exception e) {
            log.error("******************** EXCEPTION = " + e.getMessage());
        }

        // Test that the owner's supply has not been subtracted by the tokens transferred by John to himself

        BigInteger ownerSupplyAfter = johnContract.balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply after = " + ownerSupplyAfter.toString());

        assertThat(ownerSupplyAfter, equalTo(ownerSupply));

        // Test that John has 0 tokens

        BigInteger johnTokensAfter = johnContract.balanceOf(getJohnAddress()).send();
        log.info(">>>>>>>>>> John's tokens after = " + johnTokensAfter.toString());

        assertThat(johnTokensAfter, equalTo(BigInteger.ZERO));

        log.info("******************** END: test4TransferByNonBuyer()");
    }
}
