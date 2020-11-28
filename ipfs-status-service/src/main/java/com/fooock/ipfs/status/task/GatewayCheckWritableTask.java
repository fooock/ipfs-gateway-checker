package com.fooock.ipfs.status.task;

import com.fooock.ipfs.status.model.Gateway;
import com.fooock.ipfs.status.model.Report;
import com.fooock.ipfs.status.repository.ReportMemoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Check if the public gateway has public writable access
 */
@Configuration
@EnableScheduling
@Slf4j
public class GatewayCheckWritableTask {
    private final static long THREE_HOURS = 60 * 60 * 1000 * 3;
    private final static long ONE_MINUTE = 60000;

    private final ReportMemoryRepository reportMemoryRepository;
    private final WebClient webClient;

    public GatewayCheckWritableTask(ReportMemoryRepository reportMemoryRepository, WebClient webClient) {
        this.reportMemoryRepository = reportMemoryRepository;
        this.webClient = webClient;
    }

    /**
     * Execute check every three hours and save result to repository.
     */
    @Scheduled(fixedRate = THREE_HOURS, initialDelay = ONE_MINUTE)
    public void checkWritable() {
        log.debug("Prepared to check writable nodes...");

        List<Report> online = reportMemoryRepository.findOnline();
        if (online.isEmpty()) {
            log.warn("No online gateways...");
            return;
        }
        log.info("Check writable state of {} gateways...", online.size());
        online.forEach(gateway -> {

            Mono<ClientResponse> clientResponse = webClient.post()
                    .uri(buildPostUrl(gateway.getName()))
                    .exchange();

            clientResponse.subscribe(s -> checkGatewayResponse(gateway, s),
              throwable -> updateWritableState(gateway, Gateway.GATEWAY_NO_WRITABLE));
        });
        log.debug("Finish writable check!");
    }

    /**
     * Check gateway writable response. If the response is 2xx then the gateway is writable, otherwise,
     * it isn't.
     *
     * @param gateway  Current checked gateway
     * @param response Gateway response
     */
    private void checkGatewayResponse(Report gateway, ClientResponse response) {
        if (response.statusCode().is2xxSuccessful()) {
            log.debug("Gateway {} is writable (response = {})", gateway.getName(), response.rawStatusCode());
            updateWritableState(gateway, Gateway.GATEWAY_WRITABLE);
            return;
        }
        log.debug("Gateway {} is not writable (response = {})", gateway.getName(), response.rawStatusCode());
        updateWritableState(gateway, Gateway.GATEWAY_NO_WRITABLE);
    }

    private void updateWritableState(Report gateway, int writableStatus) {
        reportMemoryRepository.updateWritableState(gateway.getName(), writableStatus);
    }

    private String buildPostUrl(String name) {
        return String.format("%s/ipfs/", name);
    }
}
