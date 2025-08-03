package com.example.backend.controller;

import com.example.backend.dto.ShortenRequest;
import com.example.backend.entity.ShortUrl;
import com.example.backend.service.UrlShortnerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Map;

/**
 * A REST controller responsible for handling write operations
 * related to shortened URLs.
 * <p>
 * This controller provides endpoints to create, update,
 * and delete shortened URLs. It
 * interacts with the {@link UrlShortnerService} to perform these operations.
 *
 * @see UrlShortnerService
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/write")
@RequiredArgsConstructor
public final class WriteController {

    /**
     * Service responsible for managing URL shortening and related operations.
     */
    private final UrlShortnerService urlShortnerService;
    /**
     * Creates a new shortcode for the provided URL.
     *
     * @param request the request object containing
     *                the original URL to be shortened
     * @return a {@link ResponseEntity} containing the generated shortcode with
     *         HTTP status 201 (CREATED)
     * @see UrlShortnerService#generateShortCode(String)
     */
    @Tag(name = "post",description = "Create shortcode for Url")
    @PostMapping("/shorten")
    public ResponseEntity<?> createShortCode(@RequestBody
                                                 final ShortenRequest request) {
        ShortUrl shortUrl = urlShortnerService.generateShortCode(request.url());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shortUrl);
    }

    /**
     * Deletes a shortened URL identified by the given shortcode.
     *
     * @param shortcode the shortcode identifying
     *                  the shortened URL to delete
     * @return a {@link ResponseEntity} with HTTP status 204 (NO_CONTENT)
     * and a success message
     * @see UrlShortnerService#deleteUrlByShortCode(String)
     */
    @Tag(name = "del",description =  "Delete Url by shortcode")
    @DeleteMapping("/shorten/{shortcode}")
    public ResponseEntity<?> deleteShorten(@PathVariable
                                               final String shortcode) {
        urlShortnerService.deleteUrlByShortCode(shortcode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Short URL deleted successfully");
    }

    /**
     * Updates the original URL associated with the given shortcode.
     *
     * @param shortcode the shortcode identifying the shortened URL to update
     * @param request   the request object containing the new URL
     *                  to associate with the shortcode
     * @return a {@link ResponseEntity} with HTTP status 200 (OK)
     * and a success message
     * @see UrlShortnerService#updateUrlByShortCode(String, String)
     */
    @Tag(name = "put",description = "Update Url by shortcode")
    @PutMapping("/shorten/{shortcode}")
    public ResponseEntity<?> updateShorten(@PathVariable final String shortcode,
                                           @RequestBody final ShortenRequest
                                                   request
        ) {
        urlShortnerService.updateUrlByShortCode(shortcode, request.url());
        return ResponseEntity.ok(Map.
                of("message", "URL updated successfully"));
    }
}
