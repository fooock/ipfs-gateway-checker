package com.fooock.ipfs.status.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
public class Report {
    private long latency;
    private long lastUpdate;

    private int statusCode;
    private int writable = Gateway.GATEWAY_WRITABLE_UNKNOWN;

    private String name;
    private String url;

    private List<String> cors;
}
