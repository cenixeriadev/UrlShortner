package com.example.backend.controller;


import com.example.backend.service.UrlShortnerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/write")
public class WriteController {
    private UrlShortnerService urlShortnerService;
    
    
}
