package com.example.backend.controller;


import com.example.backend.service.UrlShortnerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * A REST controller responsible for handling read
 * operations related to shortened URLs.
 * <p>
 * This controller provides endpoints to retrieve
 * the original URL and statistics (e.g., access count)
 * associated with a given shortcode.
 * It interacts with services such as {@link UrlShortnerService}
 * @see UrlShortnerService
 */

@RestController
@Slf4j
@RequestMapping("/api/v1/read")
@RequiredArgsConstructor
public final class ReadController {

    /**
     * Service responsible for managing URL shortening and retrieval logic.
     */
    private final UrlShortnerService urlShortnerService;
    /**
     * Retrieves the original URL associated with the given shortcode.
     *
     * @param shortcode the shortcode identifying the shortened URL
     * @return a {@link ResponseEntity} containing the original URL if found
     * @see UrlShortnerService#getUrlByShortCode(String)
     */
    @Tag(name = "get",description = "Get the url by shortcode")
    @GetMapping("/shorten/{shortcode}")
    public ResponseEntity<?> getUrl(@PathVariable final String shortcode) {
        String url = urlShortnerService.getUrlByShortCode(shortcode);
        return ResponseEntity.ok(url);
    }

    /**
     * Retrieves the access count statistics for the given shortcode.
     *
     * @param shortcode the shortcode identifying the shortened URL
     * @return a {@link ResponseEntity} containing the access count of
     * the shortened URL
     * @see UrlShortnerService#getUrlByShortCode(String)
     */
    @Tag(name = "get",
            description =  "Gets the number of times the URL was accessed")
    @GetMapping("/shorten/{shortcode}/stats")
    public ResponseEntity<?> getStats(@PathVariable final String shortcode) {
        int  count = urlShortnerService.getStatsByShortCode(shortcode);
        return ResponseEntity.ok(count);
    }
}
