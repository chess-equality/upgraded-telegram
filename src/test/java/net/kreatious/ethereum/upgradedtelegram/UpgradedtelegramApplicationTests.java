package net.kreatious.ethereum.upgradedtelegram;

import net.kreatious.ethereum.upgradedtelegram.contract.generated.Token;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.admin.Admin;

import java.math.BigInteger;

import static net.kreatious.ethereum.upgradedtelegram.contract.generated.Token.load;
import static org.junit.Assert.assertTrue;

public class UpgradedtelegramApplicationTests {
	
	private final Logger log = LoggerFactory.getLogger(UpgradedtelegramApplicationTests.class);
	
	@Value("${contract.address}")
	private String contractAddress;
	
	@Value("${owner.privateKey}")
	private String ownerPrivateKey;
	@Value("${owner.address}")
	private String ownerAddress;
	
	@Value("${alice.privateKey}")
	private String alicePrivateKey;
	@Value("${alice.address}")
	private String aliceAddress;
	
	@Value("${bob.privateKey}")
	private String bobPrivateKey;
	@Value("${bob.address}")
	private String bobAddress;

	@Autowired
	private Admin admin;
	
	private Credentials owner;
	
	private Token ownerContract;
	
	public static final BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);
	public static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
	
	@Before
	public void setUp() throws Exception {
		
		// Test for blockchain connectivity
		assert(admin.netListening().sendAsync().get().isListening());
		log.info("########## Blockchain is synced");
		
		owner = Credentials.create(ownerPrivateKey);
		
		// Load contract
		ownerContract = load(contractAddress, admin, owner, GAS_PRICE, GAS_LIMIT);
		
		// Test if contract is valid
		assertTrue(ownerContract.isValid());
		log.info("########## Contract loaded and valid");
	}
	
	public String getContractAddress() {
		return contractAddress;
	}
	
	public String getOwnerPrivateKey() {
		return ownerPrivateKey;
	}
	
	public String getOwnerAddress() {
		return ownerAddress;
	}
	
	public String getAlicePrivateKey() {
		return alicePrivateKey;
	}
	
	public String getAliceAddress() {
		return aliceAddress;
	}
	
	public String getBobPrivateKey() {
		return bobPrivateKey;
	}
	
	public String getBobAddress() {
		return bobAddress;
	}
	
	public Admin getAdmin() {
		return admin;
	}
	
	public Credentials getOwner() {
		return owner;
	}
	
	public Token getOwnerContract() {
		return ownerContract;
	}
}
