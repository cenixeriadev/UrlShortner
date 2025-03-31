package com.example.backend.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// ApiGatewayConfig.java
@Configuration
public class ApiGatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("read-api", r -> r.path("/api/v1/read/**")
                        .uri("lb://backend-service"))
                .route("write-api", r -> r.path("/api/v1/write/**")
                        .uri("lb://backend-service"))
                .build();
    }
}

