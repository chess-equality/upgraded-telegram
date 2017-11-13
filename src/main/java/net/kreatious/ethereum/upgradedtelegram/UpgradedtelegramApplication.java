package net.kreatious.ethereum.upgradedtelegram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"net.kreatious.ethereum.upgradedtelegram"})
public class UpgradedtelegramApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpgradedtelegramApplication.class, args);
	}
}
