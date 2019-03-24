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
    private final static String HASH_CHECK = "Qmaisz6NMhDB51cCvNWa1GMS7LU1pAxdF4Ld6Ft9kZEP2a";
    private final static String HASH_CONSTANT = ":hash";

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
    // @Scheduled(cron = "0 0 * * * *")
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
        gatewayArray.forEach(element -> {
            String url = element.getAsString();
            gateways.add(new Gateway(extractName(url), checkUrl(url)));
        });
        return gateways;
    }

    /**
     * Create the url to check the gateway status based in the current gateway url.
     *
     * @param gatewayUrl Gateway public url
     * @return Gateway check url
     */
    private String checkUrl(String gatewayUrl) {
        return gatewayUrl.replace(HASH_CONSTANT, HASH_CHECK).trim();
    }

    /**
     * Transform the current gateway url to a readable url name. The result string is
     * the domain name without any path.
     *
     * @param url Gateway public url
     * @return Gateway domain name
     */
    private String extractName(String url) {
        return url.replace("/ipfs/:hash", "").trim();
    }
}
