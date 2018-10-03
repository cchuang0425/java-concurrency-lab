package org.iii.common;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class Cache<T> {
    private Map<UUID, T> cache;

    public Cache() {
        this.cache = new ConcurrentHashMap<>();
    }

    public Tuple2<UUID, T> create(UUID key, T value) {
        this.cache.put(key, value);
        return Tuples.of(key, value);
    }

    public Optional<T> find(UUID key) {
        return Optional.ofNullable(this.cache.get(key));
    }

    public Optional<T> remove(UUID key) {
        return Optional.ofNullable(this.cache.remove(key));
    }
}
