/**
 * This package contains configuration classes that
 * define application-level settings and integrations.
 * <p>
 * These classes are annotated with Spring
 * annotations such as {@code @Configuration} and provide
 * beans or configurations for various frameworks
 * and tools used in the application. The configurations
 * include Zookeeper integration,OpenAPI documentation,
 *  API Gateway routing, and load balancing.
 * <p>
 * Key classes in this package:
 * <ul>
 *   <li> {@link com.example.backend.config.ZookeeperConfig}: Configures
 *   integration with Apache ZooKeeper for service discovery.</li>
 *   <li>{@link com.example.backend.config.OpenApiConfig}: Configures OpenAPI
 *   documentation for the application.</li>
 *   <li>{@link com.example.backend.config.ApiGatewayConfig}: Defines API
 *   Gateway routes for request routing.</li>
 *   <li>{@link com.example.backend.config.LoadBalancerConfig}: Configures a
 *    load-balanced {@code RestTemplate} for client-side load balancing.</li>
 * </ul>
 *
 * @see org.springframework.cloud.client.discovery.EnableDiscoveryClient
 * @see org.springframework.context.annotation.Configuration
 * @see io.swagger.v3.oas.models.OpenAPI
 * @see org.springframework.cloud.gateway.route.RouteLocator
 * @see org.springframework.web.client.RestTemplate
 */
package com.example.backend.config;
