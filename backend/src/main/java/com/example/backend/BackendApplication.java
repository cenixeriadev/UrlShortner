package com.example.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * The main entry point for the backend application.
 * <p>
 * This class is annotated with:
 * <ul>
 *   <li>{@code @SpringBootApplication}: Enables Spring Boot
 *   auto-configuration, component scanning,
 *       and configuration of the application context.</li>
 *   <li>{@code @EnableDiscoveryClient}: Enables service discovery
 *   capabilities, allowing the
 *       application to register itself with a service registry
 *       (e.g., ZooKeeper or Eureka).</li>
 * </ul>
 * <p>
 * The {@code main} method initializes the application
 * by loading environment variables from a
 * `.env` file using the {@link io.github.cdimascio.dotenv.Dotenv}
 * library. It sets system properties
 * for database credentials (e.g., {@code DB_USER}, {@code DB_PASSWORD})
 * before starting the Spring
 * Boot application.
 *
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.cloud.client.discovery.EnableDiscoveryClient
 * @see io.github.cdimascio.dotenv.Dotenv
 */
@EnableDiscoveryClient
@SpringBootApplication
public class BackendApplication {
    /**
     * The main method that serves as the entry point
     * for the Spring Boot application.
     * <p>
     * This method performs the following steps:
     * <ol>
     *   <li>Loads environment variables from a `.env` file using the
     *   {@link io.github.cdimascio.dotenv.Dotenv} library.</li>
     *   <li>Sets system properties for database credentials ({@code DB_USER}
     *   and {@code DB_PASSWORD}).</li>
     *   <li>Starts the Spring Boot application using
     *   {@link SpringApplication#run(Class, String[])}.</li>
     * </ol>
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().directory(".").ignoreIfMissing().load();
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        SpringApplication.run(BackendApplication.class, args);
    }

}
