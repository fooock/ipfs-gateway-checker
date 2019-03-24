package com.fooock.ipfs.status.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 *
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true, exclude = {"startTime", "latency"})
public class Gateway {
    private final String name;

    private long startTime;
    private long latency;

    public void calculateLatency() {
        latency = System.currentTimeMillis() - startTime;
    }
}
