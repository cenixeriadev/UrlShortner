package com.example.backend.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for defining API Gateway routes in the application.
 * <p>
 * This class configures routing rules
 * for incoming requests using Spring Cloud Gateway.
 * It maps specific paths to backend services,
 * enabling request forwarding and load balancing.
 *
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.cloud.gateway.route.RouteLocator
 */
@Configuration
public  class ApiGatewayConfig {

    /**
     * Configures and provides a {@link RouteLocator} bean for the API Gateway.
     * <p>
     * This method defines routing rules for the application:
     * <ul>
     *   <li>Requests to "/api/v1/read/**" are routed
     *   to the "backend-service".</li>
     *   <li>Requests to "/api/v1/write/**" are routed
     *   to the "backend-service".</li>
     * </ul>
     *
     * @param builder the {@link RouteLocatorBuilder} used to define routes
     * @return a {@link RouteLocator} instance with the defined routing rules
     */
    @Bean
    public RouteLocator gatewayRoutes(final RouteLocatorBuilder builder) {
        return builder.routes()
                .route("read-api", r -> r.path("/api/v1/read/**")
                        .uri("lb://backend-service"))
                .route("write-api", r -> r.path("/api/v1/write/**")
                        .uri("lb://backend-service"))
                .build();
    }
}

