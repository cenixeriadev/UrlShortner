package com.example.backend.service;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;

/**
 * Abstract base service that provides common URL
 * resolution functionality.
 * <p>
 * This service acts as a template for URL
 * resolution operations, handling
 * both cached and database lookups.
 * It provides a common implementation
 * for URL resolution that can be configured
 * to either save results to
 * cache or not.
 *
 * @see RedisService
 * @see ShortUrlRepository
 */
@RequiredArgsConstructor
public abstract class BaseUrlService {
    /**
     * Repository for accessing short URL data.
     */
    private final ShortUrlRepository shortUrlRepository;
    /**
     * Service for managing cache operations.
     */
    private final RedisService redisService;

    /**
     * Resolves a URL from a shortcode using both cache
     * and database.
     * <p>
     * This method first checks the cache for
     * the URL. If not found,
     * it queries the database. The method can
     * optionally save database
     * results to cache based on the saveToCache parameter.
     *
     * @param shortCode the shortcode to resolve
     * @param saveToCache whether to save the URL
     *                    to cache if found in database
     * @return the original URL associated with the shortcode
     * @throws RuntimeException if the URL is not found
     */
    protected String resolveUrl(String shortCode, boolean saveToCache) {
        String cachedValue = redisService.getFromCache(shortCode);
        if (cachedValue != null && !cachedValue.isEmpty()) {
            shortUrlRepository.incrementAccessCount(shortCode);
            return cachedValue;
        }

        return shortUrlRepository.findByShortCode(shortCode)
                .map(shortUrl -> {
                    shortUrlRepository.incrementAccessCount(shortCode);
                    if (saveToCache) {
                        redisService.saveToCache(shortCode, shortUrl.getUrl());
                    }
                    return shortUrl.getUrl();
                })
                .orElseThrow(() -> new ResourceNotFoundException("Url not found"));
    }

    /**
     * Get the ShortUrlRepository instance.
     * @return ShortUrlRepository instance
     */
    protected ShortUrlRepository getShortUrlRepository() {
        return shortUrlRepository;
    }
    /**
     * Get the RedisService instance.
     * @return RedisService instance
     */
    protected RedisService getRedisService() {
        return redisService;
    }
}