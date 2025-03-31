package com.example.backend.service;

import com.example.backend.entity.ShortUrl;
import com.example.backend.repository.ShortUrlRepository;
import com.example.backend.utils.Base62Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
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
        return shortUrlRepository.findByShortCode(shortCode).orElseThrow(() -> new RuntimeException("Url not found"));
    }
    public String generateShortUrl(String longUrl) throws Exception {
        int sequence = zooKeeperService.getNextSequence();
        String shortCode = Base62Converter.encode(sequence);

        // Crear y guardar en la BD
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
}
