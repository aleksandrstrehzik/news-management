package ru.clevertec.cache.impl;

import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.clevertec.cache.CacheAlgorithm;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * @Author Strezhik
 */
@ConditionalOnProperty(prefix = "cache", name = "algorithm", havingValue = "LFU")
@Component
@ToString@Scope(value = SCOPE_PROTOTYPE)
public class LFU<ID, T> implements CacheAlgorithm<ID, T> {

    private final Map<ID, T> cache;
    private final Map<ID, Integer> frequencyMap;
    private final int cacheSize;

    public LFU(@Value("${cache.size}") int cacheSize) {
        this.cacheSize = cacheSize;
        this.cache = new HashMap<>();
        this.frequencyMap = new HashMap<>();
    }

    @Override
    public T get(ID id) {
        if (cache.containsKey(id)) {
            frequencyMap.put(id, getAndIncrement(id));
            return cache.get(id);
        }
        return null;
    }

    @Override
    public void post(ID id, T object) {
        if (!cache.containsKey(id) && cache.size() < cacheSize) {
            frequencyMap.put(id, 1);
            cache.put(id, object);
        }
        if (!cache.containsKey(id) && cache.size() == cacheSize) {
            ID mostRarelyUsed = getMostRarelyUsed();
            frequencyMap.remove(mostRarelyUsed);
            cache.remove(mostRarelyUsed);
            frequencyMap.put(id, 1);
            cache.put(id, object);
        }
    }

    @Override
    public void delete(ID id) {
        frequencyMap.remove(id);
        cache.remove(id);
    }

    @Override
    public void put(ID id, T object) {
        if (cache.containsKey(id)) {
            cache.put(id, object);
            frequencyMap.put(id, getAndIncrement(id));
        } else this.post(id, object);
    }

    private Integer getAndIncrement(ID id) {
        return frequencyMap.get(id) + 1;
    }

    private ID getMostRarelyUsed() {
        return frequencyMap.entrySet().stream()
                .filter(set -> set.getValue().equals(frequencyMap.values().stream()
                        .min(Integer::compareTo)
                        .orElse(null)))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

    }
}
