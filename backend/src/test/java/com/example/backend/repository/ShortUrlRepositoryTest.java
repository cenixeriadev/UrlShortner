package com.example.backend.repository;

import com.example.backend.entity.ShortUrl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ShortUrlRepositoryTest {
    @Autowired
    private ShortUrlRepository shortUrlRepository;
    @Test
    void testSaveAndFindUrl() {
        // Arrange
        String url = "https://www.example.com";
        String shortcode = "abc123";

        // Act
        ShortUrl savedUrl = new ShortUrl();
        Timestamp createdAt = Timestamp.from(Instant.now());
        Timestamp updated = Timestamp.from(Instant.now());
        savedUrl.setUrl(url);
        savedUrl.setShortCode(shortcode);
        savedUrl.setCreatedAt(createdAt);
        savedUrl.setUpdateAt(updated);

        shortUrlRepository.save(savedUrl);

        Optional<ShortUrl> foundUrl = shortUrlRepository.findByShortCode(shortcode);

        // Assert
        assertTrue(foundUrl.isPresent());
        assertEquals(url, foundUrl.get().getUrl());
    }
}
