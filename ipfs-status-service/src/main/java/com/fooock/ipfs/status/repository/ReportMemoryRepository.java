package com.fooock.ipfs.status.repository;

import com.fooock.ipfs.status.model.Report;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 *
 */
@Repository
public class ReportMemoryRepository {
    private final Map<String, Report> reportMap = Collections.synchronizedMap(new HashMap<>());

    public Report save(String key, Report report) {
        if (reportMap.containsKey(key)) {
            return reportMap.replace(key, report);
        }
        return reportMap.put(key, report);
    }

    public Collection<Report> all() {
        return Collections.unmodifiableCollection(reportMap.values());
    }

    public List<Report> findOnline() {
        List<Report> onlineGateways = new ArrayList<>();
        for (Report report : reportMap.values()) {
            if (report.getStatusCode() == 200) onlineGateways.add(report);
        }
        return onlineGateways;
    }
}
