package org.iii.mq;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Component;

@Component
public class ChannelRegistry {

    private Map<UUID, LinkedBlockingQueue<String>> channels;

    public ChannelRegistry() {
        channels = new ConcurrentHashMap<>();
    }

    public LinkedBlockingQueue<String> createChannel(UUID id, int size) {
        LinkedBlockingQueue<String> channel = new LinkedBlockingQueue<>(size);
        channels.put(id, channel);
        return channel;
    }

    public LinkedBlockingQueue<String> createChannel(UUID id) {
        return createChannel(id, 1);
    }

    public Optional<LinkedBlockingQueue<String>> findChannel(UUID id) {
        return Optional.ofNullable(channels.get(id));
    }
}
