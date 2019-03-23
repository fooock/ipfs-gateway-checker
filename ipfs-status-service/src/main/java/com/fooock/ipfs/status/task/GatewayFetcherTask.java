package com.fooock.ipfs.status.task;

import com.fooock.ipfs.status.model.Gateway;
import com.fooock.ipfs.status.repository.GatewayMemoryRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to retrieve the list of public gateways from the official IPFS repository. The list of
 * gateways is fetched every hour.
 */
@Slf4j
@Configuration
@EnableScheduling
public class GatewayFetcherTask {
    private static final String PUBLIC_GATEWAYS_URL =
            "https://raw.githubusercontent.com/ipfs/public-gateway-checker/master/gateways.json";

    private final RestTemplate restTemplate;
    private final JsonParser jsonParser;
    private final GatewayMemoryRepository memoryRepository;

    public GatewayFetcherTask(RestTemplate rest, JsonParser parser, GatewayMemoryRepository repository) {
        this.restTemplate = rest;
        this.jsonParser = parser;
        this.memoryRepository = repository;
    }

    /**
     * Obtain the public list of IPFS gateways. This task is scheduled every hour and the result
     * is saved to an in-memory database to check one by one its status.
     */
    // @Scheduled(cron = "* 0 * * * *")
    @Scheduled(fixedRate = 10000)
    public void getPublicGateways() {
        ResponseEntity<String> response = restTemplate.getForEntity(PUBLIC_GATEWAYS_URL, String.class);
        if (response.getStatusCode().isError()) {
            log.warn("Error getting response from server, status={}", response.getStatusCodeValue());
            return;
        }
        if (response.getBody() == null || response.getBody().isEmpty()) {
            log.error("Response body from {} is not valid (response={})", PUBLIC_GATEWAYS_URL, response.getBody());
            return;
        }
        try {
            List<Gateway> gateways = parseBodyResponse(response.getBody());
            log.info("Get {} gateways from public repository", gateways.size());
            memoryRepository.save(gateways);

        } catch (JsonSyntaxException | IllegalStateException e) {
            log.error("Error getting array of gateways from response", e);
        }
    }

    private List<Gateway> parseBodyResponse(String body) {
        JsonArray gatewayArray = jsonParser.parse(body).getAsJsonArray();
        List<Gateway> gateways = new ArrayList<>(gatewayArray.size());
        gatewayArray.forEach(element -> gateways.add(new Gateway(element.getAsString())));
        return gateways;
    }
}
