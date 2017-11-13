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
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

import static net.kreatious.ethereum.upgradedtelegram.contract.generated.Token.deploy;
import static net.kreatious.ethereum.upgradedtelegram.contract.generated.Token.load;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
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
	
	static final BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);
	static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
	
	@Test
	public void contextLoads() throws Exception {
		
		// Test for blockchain connectivity
		assert(admin.netListening().sendAsync().get().isListening());
		
		Credentials owner = Credentials.create(ownerPrivateKey);
		
		// Deploy contract
		// Token ownerContract = deploy(admin, owner, GAS_PRICE, GAS_LIMIT).send();  // Check contract address in testrpc console
		
		// Load contract
		Token ownerContract = load(contractAddress, admin, owner, GAS_PRICE, GAS_LIMIT);
		
		BigInteger ownerSupply = BigInteger.valueOf(500_000_000);
		
		// Test if contract is valid
		assertTrue(ownerContract.isValid());
		
		log.info(">>>>>>>>>> Total Supply = " + ownerContract.totalSupply().send());
		log.info(">>>>>>>>>> Owner Supply = " + ownerContract.balanceOf(owner.getAddress()).send());
		
		BigInteger ownerSupplyInWei = Convert.toWei(new BigDecimal(ownerSupply), Convert.Unit.ETHER).toBigInteger();
		
		assertThat(ownerContract.totalSupply().send(), equalTo(ownerSupplyInWei));
		assertThat(ownerContract.balanceOf(owner.getAddress()).send(), equalTo(ownerSupplyInWei));

		Credentials alice = Credentials.create(alicePrivateKey);
		Credentials bob = Credentials.create(bobPrivateKey);
		
		// Test transfer by Bob to himself even if he is not owner of the contract
		// Token bobContract = load(contractAddress, admin, bob, GAS_PRICE, GAS_LIMIT);
		// BigInteger bobBalanceBefore = ownerContract.balanceOf(bobAddress).send();
		// log.info(">>>>>>>>>> bobBalanceBefore = " + bobBalanceBefore);
		
		BigInteger transferToBob = BigInteger.valueOf(100_000);
		BigInteger transferToBobInWei = Convert.toWei(new BigDecimal(transferToBob), Convert.Unit.ETHER).toBigInteger();
		
		log.info(">>>>>>>>>> transferToBob = " + new BigDecimal(transferToBob).toPlainString());
		log.info(">>>>>>>>>> transferToBobInWei = " + transferToBobInWei.toString());
		
		// Test transfer to Bob by owner
		TransactionReceipt bobTransferReceipt = ownerContract.transfer(bobAddress, transferToBobInWei).send();
		// TransactionReceipt bobTransferReceipt = bobContract.transfer(bobAddress, transferToBobInWei).send();
		
		Token.TransferEventResponse bobTransferEventValues = ownerContract.getTransferEvents(bobTransferReceipt).get(0);
		// Token.TransferEventResponse bobTransferEventValues = bobContract.getTransferEvents(bobTransferReceipt).get(0);
		
		assertThat(bobTransferEventValues._from, equalTo(ownerAddress));
		// assertThat(bobTransferEventValues._from, equalTo(bobAddress));
		assertThat(bobTransferEventValues._to, equalTo(bobAddress));
		assertThat(bobTransferEventValues._value, equalTo(transferToBobInWei));
		
		log.info(">>>>>>>>>> value from transfer event = " + bobTransferEventValues._value);
		
		// BigInteger bobBalanceAfter = ownerContract.balanceOf(bobAddress).send();
		// log.info(">>>>>>>>>> bobBalanceAfter = " + bobBalanceAfter);
		// assertThat(bobBalanceBefore, equalTo(bobBalanceAfter));
		
		log.info(">>>>>>>>>> Owner Supply = " + ownerContract.balanceOf(owner.getAddress()).send());
	}
}
