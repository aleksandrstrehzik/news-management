package ru.clevertec.cache.impl;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;
import ru.clevertec.cache.CacheAlgorithm;

/**
 * @Author Strezhik
 */
@Component
public class CacheAlgorithmFactory {

    public CacheAlgorithm<?,?> getAlgorithm() {
        return getCacheAlgorithmBean();
    }

    /**
     * Gives bean of the declared type, if it exists
     * @return CacheAlgorithm<?,?> or null
     */
    @Lookup
    CacheAlgorithm<?,?> getCacheAlgorithmBean() {
        return null;
    }
}
