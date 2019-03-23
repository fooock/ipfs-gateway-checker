package com.fooock.ipfs.status.repository;

import com.fooock.ipfs.status.model.Gateway;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */
@Repository
public class GatewayMemoryRepository {
    private final List<Gateway> memoryDb = new CopyOnWriteArrayList<>();

    public void save(List<Gateway> gateways) {
        memoryDb.addAll(gateways);
    }

    public List<Gateway> all() {
        return Collections.unmodifiableList(memoryDb);
    }
}
