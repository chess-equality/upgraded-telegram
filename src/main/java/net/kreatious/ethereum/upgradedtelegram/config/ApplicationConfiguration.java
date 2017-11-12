package net.kreatious.ethereum.upgradedtelegram.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.http.HttpService;

@Configuration
public class ApplicationConfiguration {
    
    private final Logger log = LoggerFactory.getLogger(ApplicationConfiguration.class);
    
    @Value("${endpoint}")
    private String endpoint;
    
    @Bean
    public Admin admin() {
        return Admin.build(new HttpService(endpoint));
    }
}
