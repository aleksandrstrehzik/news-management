package ru.clevertec.cache.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.clevertec.cache.CacheAlgorithm;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * @Author Strezhik
 */
@Component
@Scope(value = SCOPE_PROTOTYPE)
@ConditionalOnProperty(prefix = "cache", name = "algorithm", havingValue = "LRU")
public class LRU<ID, T> implements CacheAlgorithm<ID, T> {

    /**
     * Field containing the cache
     */
    private final Map<ID, T> cache;

    public LRU(@Value("${cache.size}") int cacheSize) {
        this.cache = new LinkedHashMap<>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<ID, T> eldest) {
                return cacheSize < cache.size();
            }
        };
    }

    @Override
    public T get(ID id) {
        if (cache.containsKey(id)) {
            T removedObject = cache.remove(id);
            cache.put(id, removedObject);
            return removedObject;
        }
        return null;
    }

    @Override
    public void post(ID id, T object) {
        cache.put(id, object);
    }

    @Override
    public void delete(ID id) {
        cache.remove(id);
    }

    @Override
    public void put(ID id, T object) {
        this.post(id, object);
    }
}
