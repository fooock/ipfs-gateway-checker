package com.fooock.ipfs.status;

import com.google.gson.JsonParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 */
@SpringBootApplication
public class IpfsStatusServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpfsStatusServiceApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JsonParser getJsonParser() {
        return new JsonParser();
    }

    @Bean
    public WebClient getWebClient() {
        return WebClient.create();
    }
}
