package org.iii.common;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Component;

@Component
public class ChannelRegistry {
    private Map<UUID, LinkedBlockingQueue<?>> channels;
    private Map<Long, LinkedBlockingQueue<Boolean>> primalityFiberChannels;

    public ChannelRegistry() {
        this.channels = new ConcurrentHashMap<>();
        this.primalityFiberChannels = new ConcurrentHashMap<>();
    }

    public <T> LinkedBlockingQueue<T> create(UUID id, int size, Class<T> type) {
        LinkedBlockingQueue<T> channel = new LinkedBlockingQueue<>(size);
        this.channels.put(id, channel);
        return channel;
    }

    public <T> LinkedBlockingQueue<T> create(UUID id, Class<T> type) {
        return create(id, 1, type);
    }

    public LinkedBlockingQueue<Boolean> create(Long n) {
        LinkedBlockingQueue<Boolean> channel = new LinkedBlockingQueue<>(1);
        this.primalityFiberChannels.put(n, channel);
        return channel;
    }

    public <T> Optional<LinkedBlockingQueue<T>> find(UUID id, Class<T> type) {
        return Optional.ofNullable((LinkedBlockingQueue<T>) this.channels.get(id));
    }

    public Optional<LinkedBlockingQueue<Boolean>> find(Long n) {
        return Optional.ofNullable(this.primalityFiberChannels.get(n));
    }
}
