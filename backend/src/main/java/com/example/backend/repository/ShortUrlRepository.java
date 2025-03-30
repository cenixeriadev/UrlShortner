package com.example.backend.repository;

import com.example.backend.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    
    Optional<ShortUrl> findByShortCode(String shortCode);
}

