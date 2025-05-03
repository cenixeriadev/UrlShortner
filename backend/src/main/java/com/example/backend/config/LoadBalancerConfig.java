package com.example.backend.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for setting up a load-balanced
 * {@code RestTemplate} in the application.
 * <p>
 * This class configures a {@code RestTemplate}
 * bean with client-side load balancing capabilities,
 * allowing the application to communicate
 * with multiple instances of a service dynamically.
 *
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.web.client.RestTemplate
 * @see org.springframework.cloud.client.loadbalancer.LoadBalanced
 */
@Configuration
public class LoadBalancerConfig {

    /**
     * Configures and provides a load-balanced {@code RestTemplate} bean.
     * <p>
     * The {@code @LoadBalanced} annotation enables
     * client-side load balancing for this
     * {@code RestTemplate}, allowing it to resolve
     * service names to actual service instances
     * using a service discovery mechanism (e.g., Eureka or ZooKeeper).
     *
     * @return a load-balanced {@code RestTemplate} instance
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
