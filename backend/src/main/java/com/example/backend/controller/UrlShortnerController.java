package com.example.backend.controller;

import com.example.backend.entity.ShortUrl;
import com.example.backend.service.UrlShortnerService;
import org.springframework.web.bind.annotation.*;

// Servicio de lectura y escritura (Read and Write API)
@RestController
@RequestMapping("/api/v1")
public class UrlShortnerController {
    
    private UrlShortnerService shortnerUrlService;

    @GetMapping("/shortner/{shortcode}")
    public ShortUrl getOriginalUrl(@PathVariable String shortcode) {
        return shortnerUrlService.getUrlByShortCode(shortcode);
    }
//    @PostMapping("/shortner")
//    public ShortUrl getShortUrl(){
//        return shortnerUrlService.getShortUrl();
//    }
}
