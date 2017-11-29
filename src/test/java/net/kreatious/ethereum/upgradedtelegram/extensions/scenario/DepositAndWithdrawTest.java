package net.kreatious.ethereum.upgradedtelegram.extensions.scenario;

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
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

import static net.kreatious.ethereum.upgradedtelegram.contract.generated.Token.load;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DepositAndWithdrawTest extends UpgradedtelegramApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(DepositAndWithdrawTest.class);

    /**
     * Tests deposit of Ether to contract by owner
     *
     * @throws Exception
     */
    @Test
    public void test1DepositByOwner() throws Exception {

        log.info("******************** START: test1DepositByOwner()");

        BigInteger weiToDeposit = Convert.toWei(BigDecimal.valueOf(0.1), Convert.Unit.ETHER).toBigInteger();  // In Wei equivalent. In testnet, watch out if owner has sufficient Ether to deposit and pay gas
        log.info(">>>>>>>>>> weiToDeposit = " + weiToDeposit.toString());

        // Test first if owner has sufficient Ether to deposit

        EthGetBalance ethGetBalance = getAdmin().ethGetBalance(getOwnerAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger ownerBalance = ethGetBalance.getBalance();
        log.info(">>>>>>>>>> Owner's Ether balance in Wei = " + ownerBalance.toString());

        assertThat("Owner has insufficient Ether to deposit", ownerBalance, greaterThanOrEqualTo(weiToDeposit));

        // Do deposit
        TransactionReceipt transactionReceipt = getOwnerContract().deposit(weiToDeposit).send();
        log.info(">>>>>>>>>> deposit tx hash = " + transactionReceipt.getTransactionHash());
        log.info(">>>>>>>>>> deposit status = " + transactionReceipt.getStatus());

        // Test that deposit has succeeded
        assertEquals(transactionReceipt.getStatus(), "1");

        // Get transaction fee
        BigInteger gasUsed = transactionReceipt.getGasUsed();
        BigInteger gasPrice = getAdmin().ethGetTransactionByHash(transactionReceipt.getTransactionHash()).sendAsync().get().getTransaction().get().getGasPrice();
        BigInteger totalTxFee = gasUsed.multiply(gasPrice);

        log.info(">>>>>>>>>> deposit gas used = " + gasUsed.toString());
        log.info(">>>>>>>>>> deposit gas price = " + gasPrice.toString());
        log.info(">>>>>>>>>> deposit total tx fee = " + totalTxFee.toString());

        // Test if owner's Ether balance has been subtracted by the amount deposited including gas fee

        ownerBalance = ownerBalance.subtract(weiToDeposit.add(totalTxFee));
        log.info(">>>>>>>>>> ownerBalance minus (weiToDeposit + totalTxFee) = " + ownerBalance.toString());

        EthGetBalance ethGetBalanceAfter = getAdmin().ethGetBalance(getOwnerAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger ownerBalanceAfter = ethGetBalanceAfter.getBalance();
        log.info(">>>>>>>>>> Owner's Ether balance in Wei after = " + ownerBalanceAfter.toString());

        assertThat(ownerBalanceAfter, equalTo(ownerBalance));

        log.info("******************** END: test1DepositByOwner()");
    }

    /**
     * Tests withdraw of Ether from contract by owner
     *
     * @throws Exception
     */
    @Test
    public void test2WithdrawByOwner() throws Exception {

        log.info("******************** START: test2WithdrawByOwner()");

        // Get owner's Ether balance
        EthGetBalance ethGetBalance = getAdmin().ethGetBalance(getOwnerAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger ownerBalance = ethGetBalance.getBalance();
        log.info(">>>>>>>>>> Owner's Ether balance in Wei = " + ownerBalance.toString());

        BigInteger weiToWithdraw = Convert.toWei(BigDecimal.valueOf(0.1), Convert.Unit.ETHER).toBigInteger();  // In Wei equivalent
        log.info(">>>>>>>>>> weiToWithdraw = " + weiToWithdraw.toString());

        // Do withdrawal
        TransactionReceipt transactionReceipt = getOwnerContract().withdraw(weiToWithdraw).send();
        log.info(">>>>>>>>>> withdraw tx hash = " + transactionReceipt.getTransactionHash());
        log.info(">>>>>>>>>> withdraw status = " + transactionReceipt.getStatus());

        // Test that withdrawal has succeeded
        assertEquals(transactionReceipt.getStatus(), "1");

        // Get transaction fee
        BigInteger gasUsed = transactionReceipt.getGasUsed();
        BigInteger gasPrice = getAdmin().ethGetTransactionByHash(transactionReceipt.getTransactionHash()).sendAsync().get().getTransaction().get().getGasPrice();
        BigInteger totalTxFee = gasUsed.multiply(gasPrice);

        log.info(">>>>>>>>>> withdraw gas used = " + gasUsed.toString());
        log.info(">>>>>>>>>> withdraw gas price = " + gasPrice.toString());
        log.info(">>>>>>>>>> withdraw total tx fee = " + totalTxFee.toString());

        // Test if owner's Ether balance has been increased by the amount withdrawn minus gas fee

        ownerBalance = ownerBalance.add(weiToWithdraw).subtract(totalTxFee);
        log.info(">>>>>>>>>> ownerBalance plus weiToWithdraw minus totalTxFee = " + ownerBalance.toString());

        EthGetBalance ethGetBalanceAfter = getAdmin().ethGetBalance(getOwnerAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger ownerBalanceAfter = ethGetBalanceAfter.getBalance();
        log.info(">>>>>>>>>> Owner's Ether balance in Wei after = " + ownerBalanceAfter.toString());

        assertThat(ownerBalanceAfter, equalTo(ownerBalance));

        log.info("******************** END: test2WithdrawByOwner()");
    }

    /**
     * Tests deposit by non-contract owner
     *
     * @throws Exception
     */
    @Test
    public void test3DepositByNonOwner() throws Exception {

        log.info("******************** START: test3DepositByNonOwner()");

        // Get owner's Ether balance
        EthGetBalance ethGetBalance = getAdmin().ethGetBalance(getOwnerAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger ownerBalance = ethGetBalance.getBalance();
        log.info(">>>>>>>>>> Owner's Ether balance in Wei = " + ownerBalance.toString());

        BigInteger weiToDeposit = Convert.toWei(BigDecimal.valueOf(0.1), Convert.Unit.ETHER).toBigInteger();  // In Wei equivalent. In testnet, watch out if Bob has sufficient Ether to deposit and pay gas
        log.info(">>>>>>>>>> weiToDeposit = " + weiToDeposit.toString());

        // Test deposit as Bob

        // Bob requires his own contract instance
        Credentials bob = Credentials.create(getBobPrivateKey());
        Token bobContract = load(getContractAddress(), getAdmin(), bob, getGasPrice(), getGasLimit());

        // Do deposit
        // try - catch is for testrpc
        try {

            TransactionReceipt transactionReceipt = bobContract.deposit(weiToDeposit).send();
            log.info(">>>>>>>>>> deposit tx hash = " + transactionReceipt.getTransactionHash());
            log.info(">>>>>>>>>> deposit status = " + transactionReceipt.getStatus());

            // Test that deposit has failed
            assertEquals(transactionReceipt.getStatus(), "0");

        } catch(Exception e) {
            log.error("******************** EXCEPTION = " + e.getMessage());
        }

        // Test that the owner's Ether balance has not been increased

        EthGetBalance ethGetBalanceAfter = getAdmin().ethGetBalance(getOwnerAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger ownerBalanceAfter = ethGetBalanceAfter.getBalance();
        log.info(">>>>>>>>>> Owner's Ether balance in Wei after = " + ownerBalanceAfter.toString());

        assertThat(ownerBalanceAfter, equalTo(ownerBalance));

        log.info("******************** END: test3DepositByNonOwner()");
    }

    /**
     * Tests withdraw by non-contract owner
     *
     * @throws Exception
     */
    @Test
    public void test4WithdrawByNonOwner() throws Exception {

        log.info("******************** START: test4WithdrawByNonOwner()");

        // Get owner's Ether balance
        EthGetBalance ethGetBalance = getAdmin().ethGetBalance(getOwnerAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger ownerBalance = ethGetBalance.getBalance();
        log.info(">>>>>>>>>> Owner's Ether balance in Wei = " + ownerBalance.toString());

        BigInteger weiToWithdraw = Convert.toWei(BigDecimal.valueOf(0.1), Convert.Unit.ETHER).toBigInteger();  // In Wei equivalent. In testnet, watch out if Bob has sufficient Ether to withdraw and pay gas
        log.info(">>>>>>>>>> weiToWithdraw = " + weiToWithdraw.toString());

        // Test withdraw as Bob

        // Bob requires his own contract instance
        Credentials bob = Credentials.create(getBobPrivateKey());
        Token bobContract = load(getContractAddress(), getAdmin(), bob, getGasPrice(), getGasLimit());

        // Do withdraw
        // try - catch is for testrpc
        try {

            TransactionReceipt transactionReceipt = bobContract.withdraw(weiToWithdraw).send();
            log.info(">>>>>>>>>> withdraw tx hash = " + transactionReceipt.getTransactionHash());
            log.info(">>>>>>>>>> withdraw status = " + transactionReceipt.getStatus());

            // Test that withdraw has failed
            assertEquals(transactionReceipt.getStatus(), "0");

        } catch(Exception e) {
            log.error("******************** EXCEPTION = " + e.getMessage());
        }

        // Test that the owner's Ether balance has not been subtracted by the transaction

        EthGetBalance ethGetBalanceAfter = getAdmin().ethGetBalance(getOwnerAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger ownerBalanceAfter = ethGetBalanceAfter.getBalance();
        log.info(">>>>>>>>>> Owner's Ether balance in Wei after = " + ownerBalanceAfter.toString());

        assertThat(ownerBalanceAfter, equalTo(ownerBalance));

        log.info("******************** END: test4WithdrawByNonOwner()");
    }
}
