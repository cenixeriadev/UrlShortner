package com.example.backend.repository;

import com.example.backend.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link ShortUrl} entities in the database.
 * <p>
 * This interface extends {@link JpaRepository} to provide
 * CRUD operations for the {@code ShortUrl}
 * entity. It also includes custom query methods
 * for finding, updating, and deleting records based
 * on specific criteria.
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.example.backend.entity.ShortUrl
 */
@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, UUID> {

    /**
     * Finds a {@link ShortUrl} entity by its shortcode.
     * <p>
     * This method performs a read-only transaction to retrieve
     * the entity associated with the
     * given shortcode.
     *
     * @param shortCode the shortcode of the URL to find
     * @return an {@link Optional} containing the {@code ShortUrl}
     * entity if found, or empty otherwise
     */
    @Transactional(readOnly = true)
    Optional<ShortUrl> findByShortCode(String shortCode);

    /**
     * Deletes a {@link ShortUrl} entity by its shortcode.
     * <p>
     * This method removes the record associated
     * with the given shortcode from the database.
     *
     * @param shortCode the shortcode of the URL to delete
     */
    @Transactional
    void deleteByShortCode(String shortCode);


    /**
     * Increments the access count of a {@link ShortUrl} entity by 1.
     * <p>
     * This method updates the {@code accessCount}
     * field of the entity associated with the given
     * shortcode.
     *
     * @param shortCode the shortcode of the URL to update
     */
    @Modifying
    @Transactional
    @Query("UPDATE ShortUrl s SET s.accessCount = "
            + "s.accessCount + 1 WHERE s.shortCode = :shortCode")
    void incrementAccessCount(@Param("shortCode") String shortCode);

    /**
     * Updates the {@code updateAt} timestamp of a {@link ShortUrl} entity.
     * <p>
     * This method sets the {@code updateAt} field
     * of the entity associated with the given shortcode
     * to the specified timestamp.
     *
     * @param timenow   the new timestamp to set
     * @param shortcode the shortcode of the URL to update
     */
    @Modifying
    @Transactional
    @Query("UPDATE ShortUrl s SET s.updateAt "
            + "= :timenow WHERE s.shortCode = :shortCode")
    void updateUpdateAt(@Param("timenow") Timestamp timenow,
                        @Param("shortCode") String shortcode);

    /**
     * Updates the URL and resets the access count of a {@link ShortUrl} entity.
     * <p>
     * This method sets the {@code url} field
     * of the entity associated with the given shortcode
     * to the specified value and resets the {@code accessCount} to 0.
     *
     * @param url       the new URL to set
     * @param shortCode the shortcode of the URL to update
     */
    @Modifying
    @Transactional
    @Query("UPDATE ShortUrl s SET s.url"
            + "= :url , s.accessCount = 0 WHERE s.shortCode = :shortCode")
    void updateUrl(@Param("url") String url,
                   @Param("shortCode") String shortCode);
}
