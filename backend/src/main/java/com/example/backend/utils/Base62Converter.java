package com.example.backend.utils;


public class Base62Converter {
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = 62;
    private static final Long FIRST_RANGE_START_VALUE = 100000L;


    public static String encode(long value) {
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.insert(0, BASE62.charAt((int)value % BASE));
            value /= BASE;
        }
        return !sb.isEmpty() ? sb.toString() : FIRST_RANGE_START_VALUE.toString(); 
    }

}
