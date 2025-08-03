package com.example.backend.service;

import com.example.backend.entity.ShortUrl;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.ShortUrlRepository;
import com.example.backend.utils.ValidatorUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

/**
 * Service class for managing URL shortening operations.
 * <p>
 * This class extends {@link BaseUrlService} and provides
 * functionality for:
 * <ul>
 *     <li>Generating shortcodes for URLs</li>
 *     <li>Retrieving URLs by shortcode</li>
 *     <li>Managing URL statistics</li>
 *     <li>Updating and deleting shortened URLs</li>
 * </ul>
 * It integrates with multiple services for complete URL management.
 *
 * @see com.example.backend.service.BaseUrlService
 * @see com.example.backend.repository.ShortUrlRepository
 * @see com.example.backend.service.RedisService
 * @see com.example.backend.service.ZooKeeperService
 */
@Slf4j
@Service
public class UrlShortnerService extends BaseUrlService {

    /**
     * ZooKeeper service for generating unique shortcodes.
     */
    private final ZooKeeperService zooKeeperService;

    /**
     * Constructs a new UrlShortnerService with
     * the required dependencies.
     *
     * @param shortUrlRepository repository for URL data access
     * @param redisService service for cache operations
     * @param zooKeeperServiceContruct for generating unique shortcodes
     */
    public UrlShortnerService(ShortUrlRepository shortUrlRepository,
                              RedisService redisService,
                              ZooKeeperService zooKeeperServiceContruct) {
        super(shortUrlRepository, redisService);
        this.zooKeeperService = zooKeeperServiceContruct;
    }

    /**
     * Retrieves the original URL for a shortcode, saving it to cache.
     *
     * @param shortCode the shortcode to resolve
     * @return the original URL associated with the shortcode
     * @throws RuntimeException if the URL is not found
     */
    public String getUrlByShortCode(String shortCode) {
        return resolveUrl(shortCode, true);
    }
    /**
     * Retrieves the access count associated with the given shortcode.
     * <p>
     * This method gets the number of times a URL
     * is accessed based on a shortcode.
     * @param shortCode the shortcode identifying the URL
     * @return the {@link Optional<ShortUrl>} access count by url.
     * @throws ResourceNotFoundException if the access count does not exist
     */
    public Optional<ShortUrl> getStatsByShortCode(final String shortCode) {
        Optional<ShortUrl> tempShort = getShortUrlRepository()
                .findByShortCode(shortCode);
        if (tempShort.isPresent()) {
            return tempShort;
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
     * @return the ShortUrl object containing the shortcode and original URL
     * @throws BadRequestException if the URL is invalid or empty
     */
    @Transactional
    public ShortUrl generateShortCode(final String url) {
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
        shortUrl.setShortCode(shortCode);
        shortUrl.setUrl(url);
        shortUrl.setCreatedAt(Timestamp.from(Instant.now()));
        shortUrl.setUpdateAt(Timestamp.from(Instant.now()));

        getShortUrlRepository().save(shortUrl);
        getRedisService().saveToCache(shortCode, url);

        return shortUrl;
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
        Optional<ShortUrl> url = getShortUrlRepository().
                findByShortCode(shortCode);
        if (url != null && url.isPresent()) {
            getShortUrlRepository().deleteByShortCode(shortCode);
            getRedisService().deleteFromCache(shortCode);
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
                getShortUrlRepository().findByShortCode(shortCode);
        if (optionalUrl.isEmpty()) {
            log.error("Error updating url by short code: {}", shortCode);
            throw new ResourceNotFoundException("Shortcode doesn't exist: "
                    + shortCode);
        }
        getShortUrlRepository().updateUrl(newLongUrl, shortCode);
        getShortUrlRepository().updateUpdateAt(
                Timestamp.from(Instant.now()),
                shortCode
        );
        getRedisService().saveToCache(shortCode, newLongUrl);
    }
}
