package com.example.backend.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("read-api", r -> r.path("/read/**")
                        .uri("lb://read-api")) // Se conecta al Load Balancer
                .route("write-api", r -> r.path("/write/**")
                        .uri("lb://write-api"))
                .build();
    }
}

