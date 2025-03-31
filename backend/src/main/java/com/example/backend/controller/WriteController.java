package com.example.backend.controller;

import com.example.backend.dto.ShortenRequest;
import com.example.backend.service.UrlShortnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/write")
public class WriteController {
    
    private final UrlShortnerService urlShortnerService;
    @Autowired
    public WriteController(UrlShortnerService urlShortnerService) {
        this.urlShortnerService = urlShortnerService;
    }
    @PostMapping("/shorten")
    public ResponseEntity<?> createShortUrl(@RequestBody  ShortenRequest request){
        try {
            String shortUrl = urlShortnerService.generateShortUrl(request.url());
            return ResponseEntity.ok(shortUrl);
        } catch (Exception e) {
            log.error("Error generating short URL: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
    
}
