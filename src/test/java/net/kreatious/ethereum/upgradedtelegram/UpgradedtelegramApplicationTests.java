package net.kreatious.ethereum.upgradedtelegram;

import net.kreatious.ethereum.upgradedtelegram.contract.generated.Token;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.admin.Admin;

import java.math.BigInteger;

import static net.kreatious.ethereum.upgradedtelegram.contract.generated.Token.deploy;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpgradedtelegramApplicationTests {
	
	private final Logger log = LoggerFactory.getLogger(UpgradedtelegramApplicationTests.class);
	
	@Value("${owner.privateKey}")
	private String ownerPrivateKey;
	
	@Autowired
	private Admin admin;
	
	static final BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);
	static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
	
	@Test
	public void contextLoads() throws Exception {
		
		// Test for blockchain connectivity
		assert(admin.netListening().sendAsync().get().isListening());
		
		// Deploy contract
		Credentials owner = Credentials.create(ownerPrivateKey);
		Token contract = deploy(admin, owner, GAS_PRICE, GAS_LIMIT).send();
		
		// Test if contract is valid
		assertTrue(contract.isValid());
	}
}
