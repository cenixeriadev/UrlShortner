package com.example.backend.service;

import com.example.backend.entity.ShortUrl;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.ShortUrlRepository;
import com.example.backend.utils.Base62Converter;
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
        log.info("Looking up URL by short code: {}", shortCode);
        shortUrlRepository.incrementAccessCount(shortCode);
        return shortUrlRepository.findByShortCode(shortCode).orElseThrow(() -> new RuntimeException("Url not found"));
    }
    @Transactional
    public String generateShortUrl(String longUrl){
        log.info("This is the longURL: {}", longUrl);
        int sequence = zooKeeperService.getNextSequence();
        
        log.info("Get nextSequence");
        String shortCode = Base62Converter.encode(sequence);
        
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setId(UUID.randomUUID());
        shortUrl.setShortCode(shortCode);
        shortUrl.setOriginalUrl(longUrl);
        shortUrl.setCreatedAt(Timestamp.from(Instant.now()));
        shortUrl.setUpdateAt(Timestamp.from(Instant.now()));
        shortUrl.setAccessCount(0);

        shortUrlRepository.save(shortUrl);
        redisService.saveToCache(shortCode, longUrl);

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
            throw new ResourceNotFoundException("Shortcode doesn't exist: " + shortCode);
        }
        
    }
    @Transactional
    public void updateUrlByShortCode(String shortCode, String newLongUrl) {
        log.info("Updating URL by short code: {} to new long URL: {}", shortCode, newLongUrl);
        Optional<ShortUrl> optionalUrl = shortUrlRepository.findByShortCode(shortCode);
        if (optionalUrl.isEmpty()) {
            throw new ResourceNotFoundException("Shortcode doesn't exist: " + shortCode);
        }

        // Actualizar el originalUrl y updateAt
        shortUrlRepository.updateUrl(newLongUrl, shortCode);
        shortUrlRepository.updateUpdateAt(Timestamp.from(Instant.now()), shortCode);

        // Guardar en Redis
        redisService.saveToCache(shortCode, newLongUrl);
    }
}
