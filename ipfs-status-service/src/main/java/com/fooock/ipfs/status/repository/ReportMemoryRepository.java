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

    public synchronized Report save(String key, Report report) {
        if (reportMap.containsKey(key)) {
            return reportMap.replace(key, report);
        }
        return reportMap.put(key, report);
    }

    public Collection<Report> all() {
        return reportMap.values();
    }

    public List<Report> findOnline() {
        List<Report> onlineGateways = new ArrayList<>();
        for (Report report : reportMap.values()) {
            if (report.getStatusCode() == 200) onlineGateways.add(report);
        }
        return onlineGateways;
    }

    public boolean exists(String key) {
        return reportMap.containsKey(key);
    }

    public Report get(String key) {
        return reportMap.get(key);
    }

    public void updateWritableState(String key, int state) {
        reportMap.get(key).setWritable(state);
    }
}
