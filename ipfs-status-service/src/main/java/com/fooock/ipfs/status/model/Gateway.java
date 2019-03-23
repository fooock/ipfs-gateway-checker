package com.fooock.ipfs.status.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
public class Gateway {
    private final String name;
}
