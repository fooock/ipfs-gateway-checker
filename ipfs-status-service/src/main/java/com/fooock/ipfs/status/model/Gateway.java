package com.fooock.ipfs.status.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 *
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true, exclude = {"startTime", "latency", "lastUpdate"})
public class Gateway {
    private final String name;
    private final String url;

    private long startTime;
    private long lastUpdate;
    private long latency;

    public void calculateLatency() {
        lastUpdate = System.currentTimeMillis();
        latency = lastUpdate - startTime;
    }
}
