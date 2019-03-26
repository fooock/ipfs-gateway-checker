package com.fooock.ipfs.status.task;

import com.fooock.ipfs.status.model.Gateway;
import com.fooock.ipfs.status.model.Report;
import com.fooock.ipfs.status.repository.ReportMemoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

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
        log.info("Prepared to check writable nodes...");

        List<Report> online = reportMemoryRepository.findOnline();
        if (online.isEmpty()) {
            log.warn("No online gateways...");
            return;
        }
        log.info("Check writable state of {} gateways...", online.size());
        online.forEach(gateway -> {

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new ClassPathResource("writable.txt"));

            Mono<ClientResponse> clientResponse = webClient.post()
                    .uri(buildPostUrl(gateway.getName()))
                    .headers(httpHeaders -> httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA))
                    .body(BodyInserters.fromMultipartData(body))
                    .exchange();

            clientResponse
                    .flatMap((Function<ClientResponse, Mono<String>>) response -> response.bodyToMono(String.class))
                    .subscribe(s -> writableGateway(gateway, Gateway.GATEWAY_WRITABLE), throwable -> writableGateway(gateway, Gateway.GATEWAY_NO_WRITABLE));
        });
        log.info("Finish writable check!");
    }

    private void writableGateway(Report gateway, int writableStatus) {
        gateway.setWritable(writableStatus);
        reportMemoryRepository.save(gateway.getName(), gateway);
    }

    private String buildPostUrl(String name) {
        return String.format("%s:5001/api/v0/add", name);
    }
}
