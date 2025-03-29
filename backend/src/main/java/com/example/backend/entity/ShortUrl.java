package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;


@Entity
@Table(name = "short_urls")
@Data
public class ShortUrl {
    @Id
    @Column(name = "id"  , columnDefinition = "BINARY(16)", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    
    @Column(name = "original_url", nullable = false)
    private String originalUrl;
    
    @Column(name = "short_code" , nullable = false)
    private String shortcode;
    
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private Timestamp updateAt;
    
    @Column(name = "access_count", nullable = false)
    private Integer accessCount;
}
