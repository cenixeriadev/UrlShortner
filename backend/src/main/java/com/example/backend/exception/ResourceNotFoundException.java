package com.example.backend.exception;
/**
 * Custom exception class representing a
 * resource not found error.
 * <p>
 * This exception is thrown when a requested
 * resource cannot be found in the system.
 * It extends {@link RuntimeException} to provide a runtime
 * exception with a custom error message.
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Constructs a new {@code ResourceNotFoundException}
     * with the specified error message.
     *
     * @param message the detail message describing
     *                the resource not found error
     */
    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
