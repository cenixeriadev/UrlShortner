/**
 * This package contains REST controllers responsible
 * for handling read,
 * write and redirect operations related to shortened
 * URLs in the backend application.
 * <p>
 * The controllers interact with services such as
 * {@link com.example.backend.service.UrlShortnerService} and
 * {@link com.example.backend.service.RedirectService}
 * to perform operations like
 * retrieving URLs, generating shortcodes, updating,
 * deleting shortened URLs and handling redirections.
 * <p>
 * Key classes in this package:
 * <ul>
 *   <li>{@link com.example.backend.controller.ReadController}:
 *       Handles read operations
 *       (e.g., retrieving URLs and stats).</li>
 *   <li>{@link com.example.backend.controller.WriteController}:
 *       Handles write operations
 *       (e.g., creating, updating, and deleting shortcodes).</li>
 *   <li>{@link com.example.backend.controller.RedirectController}:
 *       Handles URL redirection operations using HTTP 302 responses.</li>
 * </ul>
 * @see com.example.backend.service.UrlShortnerService
 * @see com.example.backend.service.RedirectService
 * @see com.example.backend.dto.ShortenRequest
 */
package com.example.backend.controller;