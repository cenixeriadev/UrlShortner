package com.example.backend.controller;

import com.example.backend.entity.ShortUrl;
import com.example.backend.service.UrlShortnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Servicio de lectura y escritura (Read and Write API)
@RestController
@RequestMapping("/api/v1/read")
public class ReadController {
    
    private final UrlShortnerService urlShortnerService;

    @Autowired
    public ReadController(UrlShortnerService urlShortnerService) {
        this.urlShortnerService = urlShortnerService;
    }
    @GetMapping("/shorten/{shortcode}")
    public ResponseEntity<ShortUrl> getOriginalUrl(@PathVariable String shortcode) {
        ShortUrl shortUrl = urlShortnerService.getUrlByShortCode(shortcode);    
        return ResponseEntity.ok(shortUrl);
    }
}
