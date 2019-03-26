package com.fooock.ipfs.status.controller;

import com.fooock.ipfs.status.model.Report;
import com.fooock.ipfs.status.repository.ReportMemoryRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 *
 */
@CrossOrigin
@RestController
@RequestMapping("gateway")
public class IpfsGatewayController {
    private final ReportMemoryRepository reportMemoryRepository;

    public IpfsGatewayController(ReportMemoryRepository reportMemoryRepository) {
        this.reportMemoryRepository = reportMemoryRepository;
    }

    @GetMapping("/all")
    public Collection<Report> all() {
        return reportMemoryRepository.all();
    }

    @GetMapping("/online")
    public Collection<Report> findOnline() {
        return reportMemoryRepository.findOnline();
    }
}
