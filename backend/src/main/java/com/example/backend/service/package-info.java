/**
 * This package contains service classes that encapsulate
 * business logic and interact with
 * repositories, external systems, and utilities.
 * <p>
 * These services provide functionality for URL shortening,
 * caching, and distributed sequence
 * generation. They are annotated with {@code @Service} to
 * integrate with Spring's dependency
 * injection framework.
 * <p>
 * Key classes in this package:
 * <ul>
 *   <li>{@link com.example.backend.service.UrlShortnerService}: Manages URL
 *   shortening operations.</li>
 *   <li>{@link com.example.backend.service.RedisService}: Provides caching
 *   functionality using Redis.</li>
 *   <li>{@link com.example.backend.service.ZooKeeperService}:
 *   Manages distributed sequence generation using ZooKeeper.</li>
 * </ul>
 *
 * @see org.springframework.stereotype.Service
 */
package com.example.backend.service;
