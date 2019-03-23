package com.fooock.ipfs.status.task;

import com.fooock.ipfs.status.model.Gateway;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Slf4j
@Configuration
@EnableScheduling
public class GatewayFetcherTask {
    private static final String PUBLIC_GATEWAYS_URL =
            "https://raw.githubusercontent.com/ipfs/public-gateway-checker/master/gateways.json";

    private final RestTemplate restTemplate;
    private final JsonParser jsonParser;

    public GatewayFetcherTask(RestTemplate restTemplate, JsonParser jsonParser) {
        this.restTemplate = restTemplate;
        this.jsonParser = jsonParser;
    }

    @Scheduled(fixedRate = 5000)
    public void getGateways() {
        ResponseEntity<String> response = restTemplate.getForEntity(PUBLIC_GATEWAYS_URL, String.class);
        if (response.getBody() == null || response.getBody().isEmpty()) {
            log.error("Response from {} is not valid (response={})", PUBLIC_GATEWAYS_URL, response.getBody());
            return;
        }
        try {
            JsonElement json = jsonParser.parse(response.getBody());
            JsonArray gatewayArray = json.getAsJsonArray();
            log.info("Get {} gateways from public repository", gatewayArray.size());

            List<Gateway> gateways = new ArrayList<>(gatewayArray.size());
            gatewayArray.forEach(element -> gateways.add(new Gateway(element.getAsString())));

        } catch (IllegalStateException e) {
            log.error("Error getting array of gateways from response", e);
        }
    }
}
