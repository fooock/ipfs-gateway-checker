package com.fooock.ipfs.status.service;

import com.fooock.ipfs.status.model.Report;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class WebSocketReportService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketReportService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendReport(Report report) {
        simpMessagingTemplate.convertAndSend("/topic/report", report);
    }
}
