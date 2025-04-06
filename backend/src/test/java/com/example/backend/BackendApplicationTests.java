package com.example.backend;

import com.example.backend.entity.ShortUrl;
import com.example.backend.repository.ShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BackendApplicationTests {

    @Autowired
    private ShortUrlRepository shortUrlRepository;
    @Test
    void testSaveAndFindUrl() {
        // Arrange
        String url = "https://example.com";
        String shortcode = "abc123";

        // Act
        ShortUrl savedUrl = new ShortUrl();
        Timestamp createdAt = Timestamp.from(Instant.now());
        Timestamp updated = Timestamp.from(Instant.now());
        savedUrl.setId(UUID.randomUUID());
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

    @Test
    void contextLoads() {
    }
    

}
