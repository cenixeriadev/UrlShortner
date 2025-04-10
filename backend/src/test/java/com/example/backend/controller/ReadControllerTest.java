package com.example.backend.controller;

import com.example.backend.entity.ShortUrl;
import com.example.backend.repository.ShortUrlRepository;
import com.example.backend.service.RedisService;
import com.example.backend.service.UrlShortnerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ReadController.class)
@Import(MockBeansConfig.class)
class ReadControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UrlShortnerService urlShortnerService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ShortUrlRepository urlRepository;

    @Test
    void testGetUrlFromCache() {
        // Arrange
        String shortcode = "shortcode";
        String expectedUrl = "https://example.com";

        when(redisService.getFromCache(shortcode)).thenReturn(expectedUrl);
        doNothing().when(urlRepository).incrementAccessCount(shortcode);

        // Act & Assert
        webTestClient.get()
                .uri("/api/v1/read/shorten/{shortcode}", shortcode)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expectedUrl);

        verify(urlRepository).incrementAccessCount(shortcode);
    }

    @Test
    void testGetUrlFromDB() {
        // Arrange
        String shortcode = "shortcode";
        String expectedUrl = "https://example.com";
        ShortUrl mockShortUrl = new ShortUrl();
        mockShortUrl.setUrl(expectedUrl);

        when(redisService.getFromCache(shortcode)).thenReturn(null);
        when(urlShortnerService.getUrlByShortCode(shortcode)).thenReturn(mockShortUrl);

        // Act & Assert
        webTestClient.get()
                .uri("/api/v1/read/shorten/{shortcode}", shortcode)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expectedUrl);

        verify(redisService,atLeastOnce()).getFromCache(shortcode);
        verify(urlShortnerService,atLeastOnce()).getUrlByShortCode(shortcode);
    }

    @Test
    void testGetStats() {
        // Arrange
        String shortcode = "shortcode";
        ShortUrl mockShortUrl = new ShortUrl();
        mockShortUrl.setAccessCount(5);

        when(urlShortnerService.getUrlByShortCode(shortcode)).thenReturn(mockShortUrl);

        // Act & Assert
        webTestClient.get()
                .uri("/api/v1/read/shorten/{shortcode}/stats", shortcode)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .isEqualTo(5);
    }
}
@TestConfiguration
class MockBeansConfig {

    @Bean
    public RedisService redisService() {
        return mock(RedisService.class);
    }

    @Bean
    public UrlShortnerService urlShortnerService() {
        return mock(UrlShortnerService.class);
    }

    @Bean
    public ShortUrlRepository shortUrlRepository() {
        return mock(ShortUrlRepository.class);
    }
}