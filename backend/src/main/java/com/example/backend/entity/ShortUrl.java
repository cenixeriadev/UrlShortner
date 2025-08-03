package com.example.backend.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Data;
import java.sql.Timestamp;

/**
 * Entity representing a shortened URL in the database.
 * <p>
 * This class maps to the "short_urls" table in the database.
 * It contains information about
 * the original URL, the generated short code,
 * timestamps for creation and updates, and
 * the access count for the shortened URL.
 *
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.Table
 * @see lombok.Data
 */
@Entity
@Table(name = "short_urls")
@Data
public class ShortUrl {
    /**
     * Unique identifier for the entity, represented as a IDENTITY.
     * <p>
     * This field maps to the "id" column in the
     * "short_urls" table and cannot be null.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * The original URL that was shortened.
     * <p>
     * This field maps to the "url" column in
     * the "short_urls" table and cannot be null.
     */
    @Column(name = "url", nullable = false)
    private String url;

    /**
     * The unique shortcode generated for the URL.
     * <p>
     * This field maps to the "short_code" column in
     * the "short_urls" table, cannot be null,
     * and must be unique across all records.
     */
    @Column(name = "short_code", nullable = false, unique = true)
    private String shortCode;

    /**
     * Timestamp indicating when the record was created.
     * <p>
     * This field maps to the "created_at" column in
     * the "short_urls" table and cannot be null.
     */
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    /**
     * Timestamp indicating when the record was last updated.
     * <p>
     * This field maps to the "updated_at" column in
     * the "short_urls" table and cannot be null.
     */
    @Column(name = "updated_at", nullable = false)
    private Timestamp updateAt;

    /**
     * The number of times the shortened URL has been accessed.
     * <p>
     * This field maps to the "access_count" column in
     * the "short_urls" table, cannot be null,
     * and defaults to 0.
     */
    @Column(name = "access_count", nullable = false)
    private Integer accessCount = 0;
}
