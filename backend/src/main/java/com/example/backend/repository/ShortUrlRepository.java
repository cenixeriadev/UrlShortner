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

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, UUID> {
    @Transactional(readOnly = true) 
    Optional<ShortUrl> findByShortCode(String shortCode);
    
    @Transactional
    void deleteByShortCode(String shortCode);

    @Modifying
    @Transactional
    @Query("UPDATE ShortUrl s SET s.accessCount = s.accessCount + 1 WHERE s.shortCode = :shortCode")
    void incrementAccessCount(@Param("shortCode") String shortCode);
    
    @Modifying
    @Transactional
    @Query("UPDATE ShortUrl s SET s.updateAt = :timenow WHERE s.shortCode = :shortCode")
    void updateUpdateAt(@Param("timenow") Timestamp timenow ,@Param("shortCode") String shortcode);
    
    @Modifying
    @Transactional
    @Query("UPDATE ShortUrl s SET s.url = :url , s.accessCount = 0 WHERE s.shortCode = :shortCode")
    void updateUrl(@Param("url") String url,@Param("shortCode") String shortCode);
    
}

