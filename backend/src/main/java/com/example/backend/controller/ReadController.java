package com.example.backend.controller;

import com.example.backend.entity.ShortUrl;
import com.example.backend.repository.ShortUrlRepository;
import com.example.backend.service.RedisService;
import com.example.backend.service.UrlShortnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Servicio de lectura y escritura (Read and Write API)
@RestController
@Slf4j
@RequestMapping("/api/v1/read")
public class ReadController {
    
    private final UrlShortnerService urlShortnerService;
    private final RedisService redisService;
    private final ShortUrlRepository urlRepository;

    @Autowired
    public ReadController(UrlShortnerService urlShortnerService , RedisService redisService , ShortUrlRepository urlRepository) {
        this.urlShortnerService = urlShortnerService;
        this.redisService = redisService;
        this.urlRepository = urlRepository;
    }
    @GetMapping("/shorten/{shortcode}")
    public ResponseEntity<?> getOriginalUrl(@PathVariable String shortcode) {
        
        String cachedValue = redisService.getFromCache(shortcode);
        if (cachedValue != null && !cachedValue.isEmpty()) {
            log.info("Retrieving original url from cache");
            urlRepository.incrementAccessCount(shortcode);
            return ResponseEntity.ok(cachedValue);
        }
        log.info("Retrieving original url from database");
        ShortUrl shortUrl = urlShortnerService.getUrlByShortCode(shortcode);    
        return ResponseEntity.ok(shortUrl.getOriginalUrl());
    }
}
