/**
 * This package contains custom exception classes and a global
 * exception handler for managing errors in the application.
 * <p>
 * These classes provide a structured way to handle
 * exceptions and map them to appropriate HTTP responses.
 * The global exception handler ensures consistent error
 * handling across all controllers.
 * <p>
 * Key classes in this package:
 * <ul>
 *   <li>{@link com.example.backend.exception.BadRequestException}: Represents a
 *   bad request error.</li>
 *   <li>{@link com.example.backend.exception.ResourceNotFoundException}:
 *   Represents a
 *   resource not found error.</li>
 *   <li>{@link com.example.backend.exception.GlobalExceptionHandler}: Provides
 *   centralized handling of exceptions.</li>
 * </ul>
 *
 * @see org.springframework.web.bind.annotation.ControllerAdvice
 * @see org.springframework.web.bind.annotation.ExceptionHandler
 */
package com.example.backend.exception;
