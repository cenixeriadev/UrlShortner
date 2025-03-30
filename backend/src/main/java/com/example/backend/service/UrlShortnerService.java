package com.example.backend.service;

import com.example.backend.entity.ShortUrl;
import com.example.backend.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class UrlShortnerService {
    private final ShortUrlRepository shortUrlRepository;
    private final RedisService redisService;
    private final ZooKeeperService zooKeeperService;
    
    public ShortUrl getUrlByShortCode(String shortCode) {
        log.info("Looking up URL by short code: {}", shortCode);
        return shortUrlRepository.findByShortCode(shortCode).orElseThrow(() -> new RuntimeException("Url not found"));
    }
    
}
