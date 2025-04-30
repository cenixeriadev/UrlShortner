package com.example.backend.controller;

import com.example.backend.dto.ShortenRequest;
import com.example.backend.service.UrlShortnerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class WriteControllerTest {
    @Mock
    UrlShortnerService urlShortnerService;
    @InjectMocks
    WriteController writeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateShortUrl() {
        String url = "https://www.youtube.com/watch?v=TnTfFWwf44U";
        when(urlShortnerService.generateShortCode(url)).thenReturn("generateShortCodeResponse");

        ResponseEntity<?> result = writeController.createShortCode(new ShortenRequest(url));
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Assertions.assertEquals("generateShortCodeResponse", result.getBody());

    }

    @Test
    void testDeleteShorten() {
        ResponseEntity<?> result = writeController.deleteShorten("shortcode");

        verify(urlShortnerService).deleteUrlByShortCode("shortcode");

        Assertions.assertEquals(
                ResponseEntity.status(HttpStatus.NO_CONTENT).body("Short URL deleted successfully"),
                result
        );
    }

    @Test
    void testUpdateShorten() {
        ResponseEntity<?> result = writeController.updateShorten("shortcode", new ShortenRequest("url"));

        
        verify(urlShortnerService).updateUrlByShortCode("shortcode", "url");

        
        Assertions.assertEquals(
                ResponseEntity.ok().body("Short URL updated successfully"), 
                result
        );
    }
}

