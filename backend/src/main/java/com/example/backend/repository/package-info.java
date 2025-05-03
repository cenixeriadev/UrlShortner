/**
 * This package contains repository interfaces
 * for interacting with the database.
 * <p>
 * These interfaces extend Spring Data JPA
 * repositories to provide CRUD operations and custom
 * query methods for application entities.
 * They are annotated with {@code @Repository} to integrate
 * with Spring's data access layer.
 * <p>
 * Key interfaces in this package:
 * <ul>
 *   <li>{@link com.example.backend.repository.ShortUrlRepository}:
 *   Manages database operations for {@code ShortUrl} entities.</li>
 * </ul>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see org.springframework.stereotype.Repository
 */
package com.example.backend.repository;
