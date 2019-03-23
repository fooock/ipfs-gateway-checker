package com.fooock.ipfs.status;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 *
 */
@EnableScheduling
@SpringBootApplication
public class IpfsStatusServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(IpfsStatusServiceApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
