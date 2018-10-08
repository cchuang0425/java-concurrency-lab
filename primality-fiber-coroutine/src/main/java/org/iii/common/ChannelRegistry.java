package org.iii.common;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;
import org.springframework.stereotype.Component;

@Component
public class ChannelRegistry {

    private Map<UUID, Channel<?>> channels;

    public ChannelRegistry() {
        this.channels = new ConcurrentHashMap<>();
    }

    public <T> Channel<T> create(UUID id, int size, Class<T> type) {
        Channel<T> channel = Channels.newChannel(size);
        this.channels.put(id, channel);
        return channel;
    }

    public <T> Channel<T> create(UUID id, Class<T> type) {
        return create(id, 1, type);
    }

    public <T> Optional<Channel<T>> find(UUID id, Class<T> type) {
        return Optional.ofNullable((Channel<T>) this.channels.get(id));
    }
}
