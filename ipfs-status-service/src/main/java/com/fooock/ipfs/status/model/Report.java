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
    private int statusCode;

    private String name;

    private List<String> cors;
}
