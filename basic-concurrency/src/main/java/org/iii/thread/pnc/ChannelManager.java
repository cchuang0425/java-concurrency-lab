package org.iii.thread.pnc;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelManager {
    private Map<UUID, Channel<?>> channelMap = new ConcurrentHashMap<>();

    public UUID register(UUID key, Channel<?> channel) {
        channelMap.put(key, channel);
        return key;
    }

    public Channel<?> take(UUID key) {
        if (!channelMap.containsKey(key)) {
            throw new IllegalArgumentException("key not exists.");
        }

        return channelMap.remove(key);
    }
}
