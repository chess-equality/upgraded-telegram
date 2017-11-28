package net.kreatious.ethereum.upgradedtelegram.extensions.scenario;

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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MintTest extends UpgradedtelegramApplicationTests {

    private final Logger log = LoggerFactory.getLogger(MintTest.class);

    /**
     * Tests minting by contract owner
     *
     * @throws Exception
     */
    @Test
    public void testMintingByOwner() throws Exception {

        log.info("******************** START: testMintingByOwner()");

        BigInteger totalSupply = getOwnerContract().totalSupply().send();
        log.info(">>>>>>>>>> Token total supply before = " + totalSupply.toString());

        BigInteger ownerSupply = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply before = " + ownerSupply.toString());

        BigInteger toMint = BigInteger.valueOf(10_000_000);  // 10 million tokens, in Ether equivalent
        log.info(">>>>>>>>>> Tokens to mine in Ether equivalent = " + toMint.toString());

        BigInteger toMintInWei = Convert.toWei(new BigDecimal(toMint), Convert.Unit.ETHER).toBigInteger();  // Convert to Wei equivalent
        log.info(">>>>>>>>>> Tokens to mine in Wei equivalent = " + toMintInWei.toString());

        // Do minting
        TransactionReceipt transactionReceipt = getOwnerContract().mint(toMintInWei).send();

        Token.TransferEventResponse transferEventValues = getOwnerContract().getTransferEvents(transactionReceipt).get(0);
        log.info(">>>>>>>>>> value from transfer event = " + transferEventValues._value);

        // Test transfer event particulars
        assertThat(transferEventValues._from, equalTo("0x0000000000000000000000000000000000000000"));
        assertThat(transferEventValues._to, equalTo(getOwnerAddress()));
        assertThat(transferEventValues._value, equalTo(toMintInWei));

        // Test that the token's total supply has been increased by the minted tokens

        totalSupply = totalSupply.add(toMintInWei);

        BigInteger totalSupplyAfter = getOwnerContract().totalSupply().send();
        log.info(">>>>>>>>>> Token's total supply after = " + totalSupplyAfter.toString());

        assertThat(totalSupplyAfter, equalTo(totalSupply));

        // Test that the owner's supply has been increased by the minted tokens

        ownerSupply = ownerSupply.add(toMintInWei);

        BigInteger ownerSupplyAfter = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply after = " + ownerSupplyAfter.toString());

        assertThat(ownerSupplyAfter, equalTo(ownerSupply));

        log.info("******************** END: testMintingByOwner()");
    }

    /**
     * Tests minting by non-contract owner
     *
     * @throws Exception
     */
    @Test
    public void testMintingByNonOwner() throws Exception {

        log.info("******************** START: testMintingByNonOwner()");

        BigInteger totalSupply = getOwnerContract().totalSupply().send();
        log.info(">>>>>>>>>> Token total supply before = " + totalSupply.toString());

        BigInteger ownerSupply = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply before = " + ownerSupply.toString());

        BigInteger toMint = BigInteger.valueOf(10_000_000);  // 10 million tokens, in Ether equivalent
        log.info(">>>>>>>>>> Tokens to mint in Ether equivalent = " + toMint.toString());

        BigInteger toMintInWei = Convert.toWei(new BigDecimal(toMint), Convert.Unit.ETHER).toBigInteger();  // Convert to Wei equivalent
        log.info(">>>>>>>>>> Tokens to mint in Wei equivalent = " + toMintInWei.toString());

        // Test minting as Alice

        Credentials alice = Credentials.create(getAlicePrivateKey());

        // Alice requires her own contract instance
        Token aliceContract = load(getOwnerContract().getContractAddress(), getAdmin(), alice, GAS_PRICE, GAS_LIMIT);

        // Do minting
        // try - catch is for testrpc
        try {

            TransactionReceipt transactionReceipt = aliceContract.mint(toMintInWei).send();

            // Test that minting has failed
            assertEquals(transactionReceipt.getStatus(), "0");

            // Test that no transfer event has been fired
            assertThat("Transfer event has been fired", 0, equalTo(aliceContract.getTransferEvents(transactionReceipt).size()));

        } catch(Exception e) {
            log.error("******************** EXCEPTION = " + e.getMessage());
        }

        // Test that the token's total supply has not increased

        BigInteger totalSupplyAfter = getOwnerContract().totalSupply().send();
        log.info(">>>>>>>>>> Token's total supply after = " + totalSupplyAfter.toString());

        assertThat(totalSupplyAfter, equalTo(totalSupply));

        // Test that the owner's supply has not increased

        BigInteger ownerSupplyAfter = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply after = " + ownerSupplyAfter.toString());

        assertThat(ownerSupplyAfter, equalTo(ownerSupply));

        log.info("******************** END: testMintingByNonOwner()");
    }
}
