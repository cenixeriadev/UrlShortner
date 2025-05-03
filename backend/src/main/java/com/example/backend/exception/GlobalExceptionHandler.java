package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * Global exception handler for handling exceptions across the application.
 * <p>
 * This class is annotated with {@code @ControllerAdvice},
 * making it a global exception handler
 * for all controllers in the application.
 * It provides centralized handling of specific exceptions
 * and maps them to appropriate HTTP responses.
 *
 * @see org.springframework.web.bind.annotation.ControllerAdvice
 * @see org.springframework.web.bind.annotation.ExceptionHandler
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link ResourceNotFoundException} by returning
     * an HTTP 404 (Not Found) response.
     *
     * @param ex the exception containing the error message
     * @return a {@link ResponseEntity} with HTTP status
     * 404 and the exception message
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(
            final ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    /**
     * Handles general exceptions by returning an
     * HTTP 500 (Internal Server Error) response.
     *
     * @param ex the exception containing the error message
     * @return a {@link ResponseEntity} with HTTP status 500
     * and a generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(final Exception ex) {
        return ResponseEntity.internalServerError().body("Error interno: "
                +
                ex.getMessage());
    }

    /**
     * Handles {@link BadRequestException} by returning
     * an HTTP 400 (Bad Request) response.
     *
     * @param ex the exception containing the error message
     * @return a {@link ResponseEntity} with HTTP status 400
     * and the exception message
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(final BadRequestException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
