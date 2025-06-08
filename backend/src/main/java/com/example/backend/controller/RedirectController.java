package com.example.backend.controller;
import com.example.backend.service.RedirectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.net.URI;


/**
 * Controller responsible for
 * handling URL redirection requests.
 * <p>
 * This controller handles incoming
 * requests with shortcodes and
 * redirects users to their corresponding
 * original URLs using
 * HTTP 302 Found status code.
 *
 * @see RedirectService
 */
@Controller
public class RedirectController {
    
    /**
     * Service responsible for resolving
     * shortcodes to original URLs.
     * <p>
     * This service is used to retrieve
     * the original URL associated with
     * a given shortcode for redirection.
     */
    private final RedirectService redirectService;

    /**
     * Constructs a new RedirectController
     * with the required service.
     * @param redirectServiceConstruct the service handling
     *                                 URL resolution
     */
    public RedirectController(RedirectService redirectServiceConstruct) {
        this.redirectService = redirectServiceConstruct;
    }

    /**
     * Redirects to the original URL
     * associated with the given shortcode.
     * <p>
     * This endpoint accepts a shortcode
     * and returns an HTTP 302 response
     * with the Location header set to the original URL.
     *
     * @param shortCode the shortcode identifying
     *                  the original URL
     * @return a ResponseEntity with HTTP 302
     * status and Location header
     * @throws RuntimeException if the original URL is not found
     */
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable
                                                          String shortCode) {
        String originalUrl = redirectService.getOriginalUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
