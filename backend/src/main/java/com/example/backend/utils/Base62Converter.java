package com.example.backend.utils;

/**
 * Utility class for encoding numeric values into Base62 strings.
 * <p>
 * This class provides a static method to
 * encode a given long value into a Base62 representation.
 * The Base62 encoding uses a character set
 * consisting of lowercase letters, uppercase letters,
 * and digits (0-9).
 *
 * @see java.lang.StringBuilder
 */
public record Base62Converter() {
    /**
     * The Base62 character set used for encoding.
     */
    private static final String BASE62 =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    /**
     * The base value for Base62 encoding.
     */
    private static final int BASE = 62;
    /**
     * The fallback start value for encoding when the input is zero.
     */
    private static final Long FIRST_RANGE_START_VALUE = 100000L;

    /**
     * Encodes a given numeric value into a Base62 string.
     * <p>
     * The encoding process repeatedly divides
     * the value by the Base62 base (62) and maps the remainder
     * to a character in the Base62 character set.
     * If the resulting string is empty, a fallback value
     * is returned.
     *
     * @param value the numeric value to encode
     * @return the Base62-encoded string representation of the value
     */
    public static String encode(long value) {
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.insert(0, BASE62.charAt((int) value % BASE));
            value /= BASE;
        }
        return !sb.isEmpty() ? sb.toString()
                : FIRST_RANGE_START_VALUE.toString();
    }

}
