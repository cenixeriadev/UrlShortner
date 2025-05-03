package com.example.backend.exception;
/**
 * Custom exception class representing a bad request error.
 * <p>
 * This exception is thrown when a
 * client sends an invalid or malformed request.
 * It extends {@link RuntimeException} to provide
 * a runtime exception with a custom error message.
 */
public class BadRequestException extends RuntimeException {

    /**
     * Constructs a new {@code BadRequestException}
     * with the specified error message.
     *
     * @param message the detail message describing the bad request error
     */
    public BadRequestException(final String message) {
        super(message);
    }
}
