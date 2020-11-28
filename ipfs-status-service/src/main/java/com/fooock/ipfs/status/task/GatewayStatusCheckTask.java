package com.fooock.ipfs.status.task;

import com.fooock.ipfs.status.model.Gateway;
import com.fooock.ipfs.status.model.Report;
import com.fooock.ipfs.status.repository.GatewayMemoryRepository;
import com.fooock.ipfs.status.repository.ReportMemoryRepository;
import com.fooock.ipfs.status.service.WebSocketReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 *
 */
@Configuration
@EnableScheduling
@Slf4j
public class GatewayStatusCheckTask {
    private final static long ONE_MINUTE = 60000;
    private final static long TEN_SECONDS = 20 * 1000;

    private final GatewayMemoryRepository gatewayMemoryRepository;
    private final ReportMemoryRepository reportMemoryRepository;
    private final WebSocketReportService reportService;
    private final WebClient webClient;

    public GatewayStatusCheckTask(GatewayMemoryRepository gatewayMemoryRepository,
                                  ReportMemoryRepository reportMemoryRepository,
                                  WebSocketReportService reportService,
                                  WebClient webClient) {
        this.gatewayMemoryRepository = gatewayMemoryRepository;
        this.reportMemoryRepository = reportMemoryRepository;
        this.reportService = reportService;
        this.webClient = webClient;
    }

    /**
     * Check one by one the gateways fetched from the public gateways repository. This action is
     * scheduled every minute, and the result is a complete gateway report indicating gateway latency,
     * last update time, gateway url and cors info.
     */
    @Scheduled(fixedRate = ONE_MINUTE, initialDelay = TEN_SECONDS)
    public void check() {
        log.debug("Prepared to check gateways...");

        Set<Gateway> gateways = gatewayMemoryRepository.all();
        if (gateways.isEmpty()) {
            log.warn("No gateways found to check...");
            return;
        }
        log.info("Checking {} gateways", gateways.size());
        gateways.forEach(gateway -> {

            Mono<ClientResponse> clientResponse = webClient.head()
                    .uri(gateway.getUrl())
                    .exchange();

            clientResponse.doOnRequest(l -> gateway.setStartTime(System.currentTimeMillis()))
                    .doOnSuccess(response -> gateway.calculateLatency())
                    .flatMap((Function<ClientResponse, Mono<Report>>) response -> transform(gateway, response))
                    .subscribe(this::onSuccess, error -> onError(gateway, error));
        });
        log.debug("Finished!");
    }

    /**
     * Transform the response from the gateway to a readable structure to send to registered clients
     *
     * @param gateway  Gateway that was checked
     * @param response Web response
     * @return Report
     */
    private Mono<Report> transform(Gateway gateway, ClientResponse response) {
        Report report = new Report();
        report.setLatency(gateway.getLatency());
        report.setStatusCode(response.rawStatusCode());
        report.setName(gateway.getName());
        report.setUrl(gateway.getUrl());
        report.setLastUpdate(gateway.getLastUpdate());

        ClientResponse.Headers headers = response.headers();
        List<String> corsHeader = headers.header("Access-Control-Allow-Origin");
        report.setCors(corsHeader);

        // If exists gateway report, return the writable state
        if (reportMemoryRepository.exists(gateway.getName()))
            report.setWritable(reportMemoryRepository.get(gateway.getName()).getWritable());

        return Mono.just(report);
    }

    /**
     * This method is invoked when we get the response of the gateway and we are prepared
     * to sent the report to registered clients.
     *
     * @param report Gateway info report
     */
    private void onSuccess(Report report) {
        reportMemoryRepository.save(report.getName(), report);
        reportService.sendReport(report);
    }

    /**
     * This method is invoked when an error occurs when request the gateway.
     *
     * @param gateway Gateway that was checked
     * @param error   Request error
     */
    private void onError(Gateway gateway, Throwable error) {

    }
}
