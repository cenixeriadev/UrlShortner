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
    @Column(name = "id", columnDefinition = "UUID", nullable = false)
    private UUID id;  
    
    @Column(name = "original_url", nullable = false)
    private String originalUrl;
    
    @Column(name = "short_code" , nullable = false , unique = true)
    private String shortCode;
    
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private Timestamp updateAt;
    
    @Column(name = "access_count", nullable = false)
    private Integer accessCount = 0;
}
