package com.fooock.ipfs.status.repository;

import com.fooock.ipfs.status.model.Gateway;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
@Repository
public class GatewayMemoryRepository {
    private final Set<Gateway> memoryDb = Collections.synchronizedSet(new HashSet<>());

    public void save(List<Gateway> gateways) {
        memoryDb.addAll(gateways);
    }

    public Set<Gateway> all() {
        return Collections.unmodifiableSet(memoryDb);
    }
}
