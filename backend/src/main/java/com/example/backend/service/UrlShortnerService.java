package com.example.backend.service;

import com.example.backend.entity.ShortUrl;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.ShortUrlRepository;
import com.example.backend.utils.ValidatorUrl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing URL shortening operations.
 * <p>
 * This class provides methods to generate shortcodes,
 * retrieve URLs by shortcode, delete URLs,
 * and update URLs. It interacts with the database via
 * {@link ShortUrlRepository}, caches data
 * using {@link RedisService}, and generates unique
 * shortcodes using {@link ZooKeeperService}.
 *
 * @see com.example.backend.repository.ShortUrlRepository
 * @see com.example.backend.service.RedisService
 * @see com.example.backend.service.ZooKeeperService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UrlShortnerService {
    /**
     * Repository to interact with database and use his logic in this service.
     */
    private final ShortUrlRepository shortUrlRepository;
    /**
     * Service responsible for redis operations.
     */
    private final RedisService redisService;
    /**
     * Service responsible for generate shortcodes.
     */
    private final ZooKeeperService zooKeeperService;

    /**
     * Retrieves the original URL associated with the given shortcode.
     * <p>
     * This method first checks the Redis cache.
     * If the URL is not found in the cache, it queries
     * the database and updates the cache with the retrieved URL.
     *
     * @param shortCode the shortcode identifying the URL
     * @return the {@link String} original URL
     * @throws ResourceNotFoundException if the shortcode does not exist
     */
    public String getUrlByShortCode(final String shortCode) {
        String cachedValue = redisService.getFromCache(shortCode);
        if (cachedValue != null && !cachedValue.isEmpty()) {
            log.info("Retrieving original url from cache");
            shortUrlRepository.incrementAccessCount(shortCode);
            return cachedValue;
        }
        log.info("Retrieving original url from database");
        return shortUrlRepository.findByShortCode(shortCode)
                .map(shortUrl -> {
                    shortUrlRepository.incrementAccessCount(shortCode);
                    redisService.saveToCache(shortCode, shortUrl.getUrl());
                    return shortUrl.getUrl();
                }).orElseThrow(() ->
                        new ResourceNotFoundException("Url not found"));
    }
    /**
     * Retrieves the access count associated with the given shortcode.
     * <p>
     * This method gets the number of times a URL
     * is accessed based on a shortcode.
     * @param shortCode the shortcode identifying the URL
     * @return the {@link Integer} access count by url.
     * @throws ResourceNotFoundException if the access count does not exist
     */
    public int getStatsByShortCode(final String shortCode) {
        Optional<ShortUrl> tempShort = shortUrlRepository
                .findByShortCode(shortCode);
        if (tempShort.isPresent()) {
            return tempShort.get().getAccessCount();
        } else {
            throw new ResourceNotFoundException("Url not found");
        }
    }
    /**
     * Generates a shortcode for the given URL.
     * <p>
     * This method validates the URL, generates a
     * unique shortcode using {@link ZooKeeperService},
     * and saves the mapping in both the database and Redis cache.
     *
     * @param url the original URL to shorten
     * @return the generated shortcode
     * @throws BadRequestException if the URL is invalid or empty
     */
    @Transactional
    public String generateShortCode(final String url) {
        log.info("This is the originalURL: {}", url);
        if (url == null || url.isEmpty()) {
            log.error("Error generating short code");
            throw new BadRequestException("The URL can't be null or void");
        } else if (!ValidatorUrl.isValidURL(url)) {
            log.error("Error generating short code by invalid url");
            throw new BadRequestException("Invalid Url");
        }
        String shortCode = zooKeeperService.getNextShortCode();
        log.info("Get nextSequence");
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setId(UUID.randomUUID());
        shortUrl.setShortCode(shortCode);
        shortUrl.setUrl(url);
        shortUrl.setCreatedAt(Timestamp.from(Instant.now()));
        shortUrl.setUpdateAt(Timestamp.from(Instant.now()));
        shortUrl.setAccessCount(0);

        shortUrlRepository.save(shortUrl);
        redisService.saveToCache(shortCode, url);

        return shortCode;
    }

    /**
     * Deletes the URL associated with the given shortcode.
     * <p>
     * This method removes the URL from both the database and Redis cache.
     *
     * @param shortCode the shortcode identifying the URL
     * @throws ResourceNotFoundException if the shortcode does not exist
     */
    @Transactional
    public void deleteUrlByShortCode(final String shortCode) {
        log.info("Deleting URL by short code: {}", shortCode);
        Optional<ShortUrl> url = shortUrlRepository.findByShortCode(shortCode);
        if (url != null && url.isPresent()) {
            shortUrlRepository.deleteByShortCode(shortCode);
            redisService.deleteFromCache(shortCode);
        } else {
            log.error("Error deleting url by short code : {}", shortCode);
            throw new ResourceNotFoundException("Shortcode doesn't exist: "
                    + shortCode);
        }
    }
    /**
     * Updates the URL associated with the given shortcode.
     * <p>
     * This method updates the URL in both the database and Redis cache.
     *
     * @param shortCode  the shortcode identifying the URL
     * @param newLongUrl the new long URL to associate with the shortcode
     * @throws ResourceNotFoundException if the shortcode does not exist
     */
    @Transactional
    public void updateUrlByShortCode(final String shortCode,
                                     final String newLongUrl) {
        log.info("Updating URL by short code: {} to new long URL: {}",
                shortCode, newLongUrl);
        Optional<ShortUrl> optionalUrl =
                shortUrlRepository.findByShortCode(shortCode);
        if (optionalUrl.isEmpty()) {
            log.error("Error updating url by short code: {}", shortCode);
            throw new ResourceNotFoundException("Shortcode doesn't exist: "
                    + shortCode);
        }
        shortUrlRepository.updateUrl(newLongUrl, shortCode);
        shortUrlRepository.updateUpdateAt(
                Timestamp.from(Instant.now()),
                shortCode
        );
        redisService.saveToCache(shortCode, newLongUrl);
    }
}
