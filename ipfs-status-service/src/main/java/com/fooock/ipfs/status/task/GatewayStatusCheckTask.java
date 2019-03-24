package com.fooock.ipfs.status.task;

import com.fooock.ipfs.status.model.Gateway;
import com.fooock.ipfs.status.repository.GatewayMemoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 *
 */
@Configuration
@EnableScheduling
@Slf4j
public class GatewayStatusCheckTask {
    private final static long ONE_MINUTE = 60000;

    private final static String HASH_CHECK = "Qmaisz6NMhDB51cCvNWa1GMS7LU1pAxdF4Ld6Ft9kZEP2a";
    private final static String HASH_CONSTANT = ":hash";

    private final GatewayMemoryRepository memoryRepository;
    private final WebClient webClient;

    public GatewayStatusCheckTask(GatewayMemoryRepository memoryRepository, WebClient webClient) {
        this.memoryRepository = memoryRepository;
        this.webClient = webClient;
    }

    /**
     *
     */
    @Scheduled(fixedRate = ONE_MINUTE)
    public void check() {
        log.info("Prepared to check gateways...");

        List<Gateway> gateways = memoryRepository.all();
        if (gateways.isEmpty()) {
            log.warn("No gateways found to check...");
            return;
        }
        log.info("Checking {} gateways", gateways.size());
        gateways.forEach(gateway -> {

            Mono<ClientResponse> clientResponse = webClient.get()
                    .uri(checkUrl(gateway.getName()))
                    .exchange();

            clientResponse.doOnRequest(l -> {
                // Set start time to measure gateway latency
                gateway.setStartTime(System.currentTimeMillis());

            }).doOnSuccess(response -> gateway.calculateLatency())
                    .subscribe(response -> onSuccess(gateway, response), error -> onError(gateway, error));
        });
        log.info("Finished!");
    }

    /**
     * This method is invoked when we get the response of the gateway.
     *
     * @param gateway  Gateway that was checked
     * @param response Web response
     */
    private void onSuccess(Gateway gateway, ClientResponse response) {
        log.info("Gateway {} has {} ms latency, code={}",
                gateway.getName(), gateway.getLatency(), response.rawStatusCode());
    }

    /**
     * This method is invoked when an error occurs when request the gateway.
     *
     * @param gateway Gateway that was checked
     * @param error   Request error
     */
    private void onError(Gateway gateway, Throwable error) {
        log.warn("Gateway {} get error {}", gateway.getName(), error.getLocalizedMessage());
    }

    private String checkUrl(String gatewayUrl) {
        return gatewayUrl.replace(HASH_CONSTANT, HASH_CHECK).trim();
    }
}
