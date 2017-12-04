package net.kreatious.ethereum.upgradedtelegram.util;

import net.kreatious.ethereum.upgradedtelegram.UpgradedtelegramApplicationTests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Optional;

import static junit.framework.TestCase.fail;

@Component
public class Utils extends UpgradedtelegramApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    // Wait for a maximum of 15 minutes for the transaction receipt
    private static final int SLEEP_DURATION = 15000;
    private static final int ATTEMPTS = 60;

    /**
     * Sends Ether to a smart contract address
     *
     * @param credentials
     * @param contractAddress
     * @param amount
     * @return
     * @throws Exception
     */
    public TransactionReceipt sendEther(Credentials credentials, String contractAddress, BigInteger amount) throws Exception {

        log.info(">>>>>>>>>>>>>>>>>>> IN Utils.sendEther");

        log.info("#################### source account = " + credentials.getAddress());
        log.info("#################### smart contract address = " + contractAddress);
        log.info("#################### amount to send (Wei) = " + amount);

        // Get the next available nonce for the account
        EthGetTransactionCount ethGetTransactionCount = getAdmin().ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        // Create transaction
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce,
                getGasPrice(),
                getGasLimit(),
                contractAddress,
                amount
        );

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        TransactionReceipt transactionReceipt = null;

        try {

            EthSendTransaction ethSendTransaction = getAdmin().ethSendRawTransaction(hexValue).send();

            if (ethSendTransaction.hasError()) {

                log.info("#################### has error = " + ethSendTransaction.hasError());
                log.info("#################### error = " + ethSendTransaction.getError().getMessage());

                String error = String.format("Transaction error: id = %d, message = %s", ethSendTransaction.getError().getCode(), ethSendTransaction.getError().getMessage());
                throw new Exception(error);
            }

            String transactionHash = ethSendTransaction.getTransactionHash();
            log.info("#################### tx hash result = " + transactionHash);

            transactionReceipt = waitForTransactionReceipt(transactionHash);

            return transactionReceipt;

        } catch (Exception e) {
            log.error("******************** EXCEPTION = " + e.getMessage());
        }

        log.info(">>>>>>>>>>>>>>>>>>> LEAVING Utils.sendEther");

        return transactionReceipt;
    }

    TransactionReceipt waitForTransactionReceipt(String transactionHash) throws Exception {

        Optional<TransactionReceipt> transactionReceiptOptional = getTransactionReceipt(transactionHash, SLEEP_DURATION, ATTEMPTS);

        if (!transactionReceiptOptional.isPresent()) {
            fail("Transaction receipt not generated after " + ATTEMPTS + " attempts");
        }

        return transactionReceiptOptional.get();
    }

    private Optional<TransactionReceipt> getTransactionReceipt( String transactionHash, int sleepDuration, int attempts) throws Exception {

        Optional<TransactionReceipt> receiptOptional = sendTransactionReceiptRequest(transactionHash);

        for (int i = 0; i < attempts; i++) {

            if (!receiptOptional.isPresent()) {

                Thread.sleep(sleepDuration);
                receiptOptional = sendTransactionReceiptRequest(transactionHash);

            } else {
                break;
            }
        }

        return receiptOptional;
    }

    private Optional<TransactionReceipt> sendTransactionReceiptRequest(String transactionHash) throws Exception {

        EthGetTransactionReceipt transactionReceipt = getAdmin().ethGetTransactionReceipt(transactionHash).sendAsync().get();

        return transactionReceipt.getTransactionReceipt();
    }
}
