package com.fooock.ipfs.status.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 */
@Service
public class GatewayFetcher {
    private static final String PUBLIC_GATEWAYS_URL =
            "https://raw.githubusercontent.com/ipfs/public-gateway-checker/master/gateways.json";

    private final RestTemplate restTemplate;

    public GatewayFetcher(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 5000)
    public void getGateways() {
        String response = restTemplate.getForObject(PUBLIC_GATEWAYS_URL, String.class);
        System.out.println(response);
    }
}
