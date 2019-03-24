package com.fooock.ipfs.status.repository;

import com.fooock.ipfs.status.model.Report;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
}
