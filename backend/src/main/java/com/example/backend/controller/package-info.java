/**
 * This package contains REST controllers responsible for handling read and
 * write operations related to shortened URLs in the backend application.
 * <p>
 * The controllers interact with services such as
 * {@link com.example.backend.service.UrlShortnerService}
 * to perform operations like
 * retrieving URLs, generating shortcodes, updating,
 * and deleting shortened URLs.
 * <p>
 * Key classes in this package:
 * <ul>
 *   <li>{@link com.example.backend.controller.ReadController}:
 *       Handles read operations
 *       (e.g., retrieving URLs and stats).</li>
 *   <li>{@link com.example.backend.controller.WriteController}:
 *        Handles write operations
 *       (e.g., creating, updating, and deleting shortcodes).</li>
 * </ul>
 * @see com.example.backend.service.UrlShortnerService
 * @see com.example.backend.dto.ShortenRequest
 */
package com.example.backend.controller;
