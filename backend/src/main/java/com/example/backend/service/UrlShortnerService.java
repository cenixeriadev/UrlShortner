package com.example.backend.service;

import com.example.backend.entity.ShortUrl;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.ShortUrlRepository;
import com.example.backend.utils.ValidaterUrl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlShortnerService {
    
    private final ShortUrlRepository shortUrlRepository;
    private final RedisService redisService;
    private final ZooKeeperService zooKeeperService;
    
    public ShortUrl getUrlByShortCode(String shortCode) {
        String cachedValue = redisService.getFromCache(shortCode);
        if (cachedValue != null && !cachedValue.isEmpty()) {
            log.info("Retrieving original url from cache");
            shortUrlRepository.incrementAccessCount(shortCode);
            ShortUrl Url = new ShortUrl();
            Url.setShortCode(shortCode);
            Url.setUrl(cachedValue);
            return Url;
        }
        log.info("Retrieving original url from database");
        return shortUrlRepository.findByShortCode(shortCode)
                .map(shortUrl->{
                    shortUrlRepository.incrementAccessCount(shortCode);
                    redisService.saveToCache(shortCode , shortUrl.getUrl());
                    return shortUrl;
                }).orElseThrow(()-> new ResourceNotFoundException("Url not found"));
    }//TODO: Enhance the logic in this function
    @Transactional
    public String generateShortCode(String url){
        log.info("This is the originalURL: {}", url);
        if ( url == null || url.isEmpty()) {
            log.error("Error generating short code");
            throw new BadRequestException("The URL can't be null or void");
        }else if(!ValidaterUrl.isValidURL(url)) {
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
    @Transactional
    public void deleteUrlByShortCode(String shortCode) {
        log.info("Deleting URL by short code: {}", shortCode);
        Optional<ShortUrl> url = shortUrlRepository.findByShortCode(shortCode);
        if(url != null && url.isPresent()) {
            shortUrlRepository.deleteByShortCode(shortCode);
            redisService.deleteFromCache(shortCode);
        }
        else {
            log.error("Error deleting url by short code : {}" , shortCode);
            throw new ResourceNotFoundException("Shortcode doesn't exist: " + shortCode);
        }
        
    }
    @Transactional
    public void updateUrlByShortCode(String shortCode, String newLongUrl) {
        log.info("Updating URL by short code: {} to new long URL: {}", shortCode, newLongUrl);
        Optional<ShortUrl> optionalUrl = shortUrlRepository.findByShortCode(shortCode);
        if (optionalUrl.isEmpty()) {
            log.error("Error updating url by short code: {}", shortCode);
            throw new ResourceNotFoundException("Shortcode doesn't exist: " + shortCode );
        }

        // Actualizar el originalUrl y updateAt
        shortUrlRepository.updateUrl(newLongUrl, shortCode);
        shortUrlRepository.updateUpdateAt(Timestamp.from(Instant.now()), shortCode);

        // Guardar en Redis
        redisService.saveToCache(shortCode, newLongUrl);
    }
}
