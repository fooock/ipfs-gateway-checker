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
    public static final int GATEWAY_WRITABLE = 0;
    public static final int GATEWAY_NO_WRITABLE = 1;
    public static final int GATEWAY_WRITABLE_UNKNOWN = 2;

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
