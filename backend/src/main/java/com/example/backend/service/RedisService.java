package com.example.backend.service;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import java.util.concurrent.TimeUnit;

/**
 * Service class for interacting with Redis as a caching layer.
 * <p>
 * This class provides methods to save, retrieve,
 * and delete data in Redis. It uses the
 * {@link StringRedisTemplate} to interact with Redis and
 * supports time-to-live (TTL) for cache entries.
 *
 * @see org.springframework.data.redis.core.StringRedisTemplate
 */
@Service
public final class RedisService {
    /**
     * Template for operations with redis.
     */
    private final StringRedisTemplate rsTemplate;

    /**
     * Constructs a new {@code RedisService} instance.
     *
     * @param redisTemplate the {@link StringRedisTemplate} for
     *                      interacting with Redis
     */
    public RedisService(final StringRedisTemplate redisTemplate) {
        this.rsTemplate = redisTemplate;
    }

    /**
     * Saves a key-value pair in Redis with a TTL of 1 day.
     *
     * @param shortCode the key to save
     * @param url       the value to associate with the key
     */
    public void saveToCache(final String shortCode, final String url) {
        rsTemplate.opsForValue().set(shortCode, url, 1, TimeUnit.DAYS);
    }

    /**
     * Retrieves the value associated with the given key from Redis.
     *
     * @param shortCode the key to retrieve
     * @return the value associated with the key,
     * or {@code null} if the key does not exist
     */
    public String getFromCache(final String shortCode) {
        return rsTemplate.opsForValue().get(shortCode);
    }

    /**
     * Deletes the key-value pair associated with the given key from Redis.
     *
     * @param shortCode the key to delete
     */
    public void deleteFromCache(final String shortCode) {
        rsTemplate.delete(shortCode);
    }
}

