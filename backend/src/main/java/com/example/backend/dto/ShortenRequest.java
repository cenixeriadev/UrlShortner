package com.example.backend.dto;


import jakarta.validation.constraints.NotBlank;

public record ShortenRequest(
        @NotBlank(message = "URL is required")  // Validación
        String url
) {}
