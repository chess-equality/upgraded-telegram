package net.kreatious.ethereum.upgradedtelegram;

import net.kreatious.ethereum.upgradedtelegram.contract.generated.Token;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.Credentials;

import static net.kreatious.ethereum.upgradedtelegram.contract.generated.Token.deploy;
import static net.kreatious.ethereum.upgradedtelegram.contract.generated.Token.load;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmartContractDeployer extends UpgradedtelegramApplicationTests {
    
    private final Logger log = LoggerFactory.getLogger(SmartContractDeployer.class);
    
    @Override
    @Before
    public void setUp() throws Exception {
        // Do not load smart contract as it is not yet deployed
        // super.setUp();
    }
    
    /**
     * Deploys smart contract by the owner as a test. Run this once if smart contract has not yet been deployed.
     *
     * @throws Exception
     */
    @Test
    public void testDeploySmartContract() throws Exception {
    
        log.info("******************** START: testDeploySmartContract()");
    
        // Test for blockchain connectivity
        assert(getAdmin().netListening().sendAsync().get().isListening());
        log.info("########## Blockchain is synced");

        // Deploy contract
        Credentials owner = Credentials.create(getOwnerPrivateKey());
        Token ownerContract = deploy(getAdmin(), owner, getGasPrice(), getGasLimit()).send();

        // Print contract address
        String contractAddress = ownerContract.getContractAddress();
        log.info(">>>>>>>>>> Contract address = " + contractAddress);
    
        ownerContract = load(contractAddress, getAdmin(), owner, getGasPrice(), getGasLimit());
    
        // Test if contract is valid
        assertTrue(ownerContract.isValid());
        log.info("########## Contract loaded and valid");
    
        log.info("******************** START: testDeploySmartContract()");
    }
}
