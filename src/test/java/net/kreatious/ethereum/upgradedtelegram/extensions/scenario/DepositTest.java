package net.kreatious.ethereum.upgradedtelegram.extensions.scenario;

import net.kreatious.ethereum.upgradedtelegram.UpgradedtelegramApplicationTests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DepositTest extends UpgradedtelegramApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(DepositTest.class);

    /**
     * Tests deposit of Ether to contract by owner
     *
     * @throws Exception
     */
    @Test
    public void testDeposit() throws Exception {

        log.info("******************** START: Test deposit");

        BigInteger weiToDeposit = Convert.toWei(BigDecimal.valueOf(0.01), Convert.Unit.ETHER).toBigInteger();  // In testnet, watch out if owner has Ether to deposit and pay gas
        log.info(">>>>>>>>>> weiToDeposit = " + weiToDeposit.toString());

        // Test first if owner has sufficient Ether to deposit

        EthGetBalance ethGetBalance = getAdmin().ethGetBalance(getOwnerAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger ownerBalance = ethGetBalance.getBalance();
        log.info(">>>>>>>>>> Owner's Ether balance in Wei = " + ownerBalance.toString());

        assertThat("Owner has insufficient Ether to deposit", ownerBalance, greaterThanOrEqualTo(weiToDeposit));

        // Do deposit
        TransactionReceipt transactionReceipt = getOwnerContract().deposit(weiToDeposit).send();

        BigInteger gasUsed = transactionReceipt.getGasUsed();
        BigInteger gasPrice = getAdmin().ethGetTransactionByHash(transactionReceipt.getTransactionHash()).sendAsync().get().getTransaction().get().getGasPrice();
        BigInteger totalTxFee = gasUsed.multiply(gasPrice);

        log.info(">>>>>>>>>> deposit tx hash = " + transactionReceipt.getTransactionHash());
        log.info(">>>>>>>>>> deposit gas used = " + gasUsed.toString());
        log.info(">>>>>>>>>> deposit gas price = " + gasPrice.toString());
        log.info(">>>>>>>>>> deposit total tx fee = " + totalTxFee.toString());

        // Test if owner's balance has been subtracted by the amount deposited including gas used

        ownerBalance = ownerBalance.subtract(weiToDeposit.add(totalTxFee));
        log.info(">>>>>>>>>> ownerBalance minus (weiToDeposit + totalTxFee) = " + ownerBalance.toString());

        EthGetBalance ethGetBalanceAfter = getAdmin().ethGetBalance(getOwnerAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger ownerBalanceAfter = ethGetBalanceAfter.getBalance();
        log.info(">>>>>>>>>> Owner's Ether balance in Wei after = " + ownerBalanceAfter.toString());

        assertThat(ownerBalanceAfter, equalTo(ownerBalance));

        log.info("******************** END: Test deposit");
    }
}
