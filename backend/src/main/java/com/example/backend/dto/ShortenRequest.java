package com.example.backend.dto;
import jakarta.validation.constraints.NotBlank;

/**
 * A Data Transfer Object (DTO) representing a request to shorten a URL.
 * <p>
 * This record encapsulates the data required
 * to generate a shortened URL. It includes
 * validation to ensure that the provided URL is not blank.
 *
 * @param url the original URL to be shortened; must not be blank
 *
 * @see jakarta.validation.constraints.NotBlank
 */
public record ShortenRequest(
        @NotBlank(message = "URL is required")
        String url
) {
}
