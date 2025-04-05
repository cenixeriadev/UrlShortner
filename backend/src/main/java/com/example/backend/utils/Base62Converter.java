package com.example.backend.utils;

public class Base62Converter {
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = 62;

    public static String encode(int value) {
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.insert(0, BASE62.charAt(value % BASE));
            value /= BASE;
        }
        return !sb.isEmpty() ? sb.toString() : "0"; 
    }

}
