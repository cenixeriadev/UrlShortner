package com.example.backend.utils;

public class Base62Converter {
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = 62;

    public static String encode(long value) {
        StringBuilder shortCode = new StringBuilder();
        while (value > 0) {
            shortCode.insert(0, BASE62.charAt((int) (value % BASE)));
            value /= BASE;
        }
        return shortCode.toString();
    }

    public static long decode(String shortCode) {
        long value = 0;
        for (char c : shortCode.toCharArray()) {
            value = value * BASE + BASE62.indexOf(c);
        }
        return value;
    }
}
