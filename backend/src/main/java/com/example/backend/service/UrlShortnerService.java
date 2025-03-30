package com.example.backend.service;

import com.example.backend.entity.ShortUrl;
import com.example.backend.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlShortnerService {
    private final ShortUrlRepository shortUrlRepository;

    @Autowired
    public UrlShortnerService(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }
    public ShortUrl getUrlByShortCode(String shortCode) {
        return shortUrlRepository.findByShortCode(shortCode).orElseThrow(() -> new RuntimeException("Url not found"));
    }
    
}
