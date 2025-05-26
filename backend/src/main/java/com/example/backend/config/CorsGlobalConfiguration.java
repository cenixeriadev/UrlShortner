package com.example.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.context.annotation.Bean;

/**
 * Global CORS configuration class.
 *
 * <p>This class defines a {@link CorsWebFilter} bean to handle
 * Cross-Origin Resource Sharing (CORS)
 * for the entire application. It allows HTTP requests
 * from the frontend hosted at
 * {@code http://localhost:4200} to access backend resources.</p>
 *
 * <p>The configuration allows all headers and HTTP methods,
 * and supports credentials (e.g., cookies,
 * authorization headers).</p>
 */
@Configuration
public class CorsGlobalConfiguration {

    /**
     * Creates a {@link CorsWebFilter} bean that applies
     * the global CORS policy.
     *
     * @return a {@code CorsWebFilter} instance
     * with the specified configuration
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedOrigin("http://localhost:4200");
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
