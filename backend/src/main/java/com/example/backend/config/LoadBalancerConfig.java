package com.example.backend.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class LoadBalancerConfig {

    @Bean
    @LoadBalanced  // Habilita el balanceo de carga automático
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

