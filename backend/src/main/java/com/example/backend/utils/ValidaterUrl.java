package com.example.backend.utils;

import java.net.URI;
import java.net.URISyntaxException;

public class ValidaterUrl {
    public static boolean isValidURL(String url) {
        try {
            URI uri = new URI(url);
            return uri.getScheme() != null && uri.getHost() != null;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}

