package net.kreatious.ethereum.upgradedtelegram.extensions.scenario;

import net.kreatious.ethereum.upgradedtelegram.UpgradedtelegramApplicationTests;
import net.kreatious.ethereum.upgradedtelegram.contract.generated.Token;
import net.kreatious.ethereum.upgradedtelegram.util.Utils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

import static net.kreatious.ethereum.upgradedtelegram.contract.generated.Token.load;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FallbackPurchaseTest extends UpgradedtelegramApplicationTests {

    private final Logger log = LoggerFactory.getLogger(FallbackPurchaseTest.class);

    @Value("${tokensPerWei}")
    private String tokensPerWeiProp;

    @Autowired
    private Utils utils;

    /**
     * Tests the fallback purchase function by sending Ether to the smart contract
     *
     * @throws Exception
     */
    @Test
    public void test1SendEther() throws Exception {

        log.info("******************** START: test1SendEther()");

        BigInteger ownerSupply = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply before = " + ownerSupply.toString());

        BigInteger aliceTokens = getOwnerContract().balanceOf(getAliceAddress()).send();
        log.info(">>>>>>>>>> Alice's tokens before = " + aliceTokens.toString());

        BigInteger tokensPerWei = BigInteger.valueOf(Long.parseLong(tokensPerWeiProp));
        BigInteger weiToPurchase = BigInteger.valueOf(1_000_000);  // In Wei equivalent. In testnet, watch out if Alice has sufficient Ether to purchase and pay gas

        BigInteger totalTokensToPurchase = weiToPurchase.multiply(tokensPerWei);
        log.info(">>>>>>>>>> totalTokensToPurchase = " + totalTokensToPurchase.toString());

        // Test first if Alice has sufficient Ether to purchase the number of tokens

        EthGetBalance ethGetBalance = getAdmin().ethGetBalance(getAliceAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        log.info(">>>>>>>>>> Alice's Ether balance in Wei = " + ethGetBalance.getBalance().toString());
        log.info(">>>>>>>>>> Tokens to purchase in Wei = " + weiToPurchase.toString());

        assertThat("Alice has insufficient Ether to purchase the tokens", ethGetBalance.getBalance(), greaterThanOrEqualTo(weiToPurchase));

        // Alice requires her own contract instance
        Credentials alice = Credentials.create(getAlicePrivateKey());
        Token aliceContract = load(getContractAddress(), getAdmin(), alice, getGasPrice(), getGasLimit());

        // Send Ether to contract
        TransactionReceipt transactionReceipt = utils.sendEther(alice, getContractAddress(), weiToPurchase);
        log.info(">>>>>>>>>> purchase tx hash = " + transactionReceipt.getTransactionHash());
        log.info(">>>>>>>>>> purchase status = " + transactionReceipt.getStatus());

        // Test that transfer has succeeded
        assertEquals(transactionReceipt.getStatus(), "1");

        Token.TransferEventResponse transferEventValues = aliceContract.getTransferEvents(transactionReceipt).get(0);
        log.info(">>>>>>>>>> value from transfer event = " + transferEventValues._value);

        // Test transfer event particulars
        assertThat(transferEventValues._from, equalTo(getOwnerAddress()));
        assertThat(transferEventValues._to, equalTo(getAliceAddress()));
        assertThat(transferEventValues._value, equalTo(totalTokensToPurchase));

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

        log.info("******************** END: test1SendEther()");
    }

    /**
     * Tests sending 0 Ether to the fallback purchase function
     *
     * @throws Exception
     */
    @Test
    public void test2SendZeroEther() throws Exception {

        log.info("******************** START: test2SendZeroEther()");

        BigInteger ownerSupply = getOwnerContract().balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply before = " + ownerSupply.toString());

        BigInteger aliceTokens = getOwnerContract().balanceOf(getAliceAddress()).send();
        log.info(">>>>>>>>>> Alice's tokens before = " + aliceTokens.toString());

        BigInteger tokensPerWei = BigInteger.valueOf(Long.parseLong(tokensPerWeiProp));
        BigInteger weiToPurchase = BigInteger.ZERO;

        BigInteger totalTokensToPurchase = weiToPurchase.multiply(tokensPerWei);
        log.info(">>>>>>>>>> totalTokensToPurchase = " + totalTokensToPurchase.toString());

        // Alice requires her own contract instance
        Credentials alice = Credentials.create(getAlicePrivateKey());
        Token aliceContract = load(getContractAddress(), getAdmin(), alice, getGasPrice(), getGasLimit());

        // Send Ether to contract
        TransactionReceipt transactionReceipt = utils.sendEther(alice, getContractAddress(), weiToPurchase);

        if (transactionReceipt != null) {

            log.info(">>>>>>>>>> purchase tx hash = " + transactionReceipt.getTransactionHash());
            log.info(">>>>>>>>>> purchase status = " + transactionReceipt.getStatus());

            // Test that transfer has not succeeded
            assertEquals(transactionReceipt.getStatus(), "0");
        }

        // Test that the owner's supply has not been subtracted by the tokens purchased by Alice

        BigInteger ownerSupplyAfter = aliceContract.balanceOf(getOwnerAddress()).send();
        log.info(">>>>>>>>>> Owner's supply after = " + ownerSupplyAfter.toString());

        assertThat(ownerSupplyAfter, equalTo(ownerSupply));

        // Test that Alice's tokens have not been increased by the purchased tokens

        BigInteger aliceTokensAfter = aliceContract.balanceOf(getAliceAddress()).send();
        log.info(">>>>>>>>>> Alice's tokens after = " + aliceTokensAfter.toString());

        assertThat(aliceTokensAfter, equalTo(aliceTokens));

        log.info("******************** END: test2SendZeroEther()");
    }
}
