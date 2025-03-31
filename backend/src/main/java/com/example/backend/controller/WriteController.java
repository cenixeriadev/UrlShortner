package com.example.backend.controller;


import com.example.backend.service.UrlShortnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/write")
public class WriteController {
    private UrlShortnerService urlShortnerService;
    
    @PostMapping("/shorten")
    public String createShortUrl(String url){
        try {
            return urlShortnerService.generateShortUrl(url);
        } catch (Exception e) {
            log.error("Error generating short URL: {}", e.getMessage());
            return "Failed to generate short URL";
        }
    }
    
}
