package org.iii.math;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.iii.common.Cache;
import org.iii.common.IdMap;

@Service
public class SystemCache {
    @Autowired
    private Cache<FibCommand> fibCache;

    @Autowired
    private Cache<PrimeCommand> primeCache;

    @Autowired
    private IdMap<UUID, Long> fibMap;

    @Autowired
    private IdMap<UUID, UUID> primeMap;

    public Cache<FibCommand> getFibCache() {
        return fibCache;
    }

    public Cache<PrimeCommand> getPrimeCache() {
        return primeCache;
    }

    public IdMap<UUID, Long> getFibMap() {
        return fibMap;
    }

    public IdMap<UUID, UUID> getPrimeMap() {
        return primeMap;
    }
}
