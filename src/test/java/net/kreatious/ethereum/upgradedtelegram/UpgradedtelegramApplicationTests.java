package net.kreatious.ethereum.upgradedtelegram;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.protocol.admin.Admin;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpgradedtelegramApplicationTests {
	
	private final Logger log = LoggerFactory.getLogger(UpgradedtelegramApplicationTests.class);
	
	@Value("${endpoint}")
	private String endpoint;
	
	@Autowired
	private Admin admin;
	
	@Test
	public void contextLoads() throws Exception {
		
		// Test for blockchain connectivity
		assert(admin.netListening().sendAsync().get().isListening());
	}
}
