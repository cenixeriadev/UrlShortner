package com.example.backend.controller;

import com.example.backend.dto.ShortenRequest;

import com.example.backend.service.UrlShortnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;





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
    public ResponseEntity<?> createShortCode(@RequestBody  ShortenRequest request) {
        String shortCode = urlShortnerService.generateShortCode(request.url());
        return ResponseEntity.status(HttpStatus.CREATED).body(shortCode);
    }
    @DeleteMapping("/shorten/{shortcode}")
    public ResponseEntity<?> deleteShorten(@PathVariable  String shortcode){
        urlShortnerService.deleteUrlByShortCode(shortcode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Short URL deleted successfully");
        
    }
    @PutMapping("/shorten/{shortcode}")
    public ResponseEntity<?> updateShorten(@PathVariable String shortcode, @RequestBody ShortenRequest request) {
        urlShortnerService.updateUrlByShortCode(shortcode, request.url());
        return ResponseEntity.ok("Short URL updated successfully");

        
    }
}
