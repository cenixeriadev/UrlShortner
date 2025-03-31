package com.example.backend.config;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient  // Habilita el descubrimiento de servicios en ZooKeeper
public class ZookeeperConfig {
}