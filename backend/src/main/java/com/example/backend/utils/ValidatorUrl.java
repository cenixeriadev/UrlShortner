package com.example.backend.utils;

import java.net.URI;
import java.net.URISyntaxException;
/**
 * Utility class for validating URLs.
 * <p>
 * This class provides a static method
 * to check whether a given string is a valid URL.
 * It uses the {@link java.net.URI} class
 * to parse and validate the URL structure.
 *
 * @see java.net.URI
 */
public record ValidatorUrl() {

    /**
     * Validates whether the given string is a valid URL.
     * <p>
     * A URL is considered valid if it
     * has a non-null scheme (e.g., "http", "https") and host.
     *
     * @param url the URL string to validate
     * @return {@code true} if the URL is valid,
     * {@code false} otherwise
     */
    public static boolean isValidURL(final String url) {
        try {
            URI uri = new URI(url);
            return uri.getScheme() != null && uri.getHost() != null;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}

