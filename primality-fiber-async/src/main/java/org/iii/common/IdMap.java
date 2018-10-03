package org.iii.common;

import java.util.Optional;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class IdMap<K, V> {
    private BiMap<K, V> idMap;

    public IdMap() {
        this.idMap = HashBiMap.create();
    }

    public V put(K key, V value) {
        return this.idMap.put(key, value);
    }

    public Optional<V> find(K key) {
        return Optional.ofNullable(idMap.get(key));
    }

    public Optional<V> remove(K key) {
        return Optional.ofNullable(idMap.remove(key));
    }
}
