package ru.clevertec.cache;

public interface CacheAlgorithm<ID, T> {

    /**
     * Method returning a value from the cache
     *
     * @param id - requested user with id
     * @return User or null
     */
    T get(ID id);

    /**
     * Adds an element to the cache.
     *
     * @param id     - object id
     * @param object - object to add
     */
    void post(ID id, T object);

    /**
     * Delete an element from the cache
     * @param id - object id
     */
    void delete(ID id);
    void put(ID id, T object);

}
