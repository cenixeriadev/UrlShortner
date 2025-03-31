package com.example.backend.controller;

import com.example.backend.entity.ShortUrl;
import com.example.backend.service.UrlShortnerService;
import org.springframework.web.bind.annotation.*;

// Servicio de lectura y escritura (Read and Write API)
@RestController
@RequestMapping("/api/v1/read")
public class ReadController {
    
    private UrlShortnerService shortnerUrlService;

    @GetMapping("/shorten/{shortcode}")
    public ShortUrl getOriginalUrl(@PathVariable String shortcode) {
        return shortnerUrlService.getUrlByShortCode(shortcode);
    }
}
