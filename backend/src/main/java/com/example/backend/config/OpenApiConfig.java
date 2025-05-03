package com.example.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for setting up OpenAPI documentation in the application.
 * <p>
 * This class configures the OpenAPI specification
 * to provide API documentation for the application.
 * It defines metadata such as the title, version,
 * description, and server URLs for the API.
 *
 * @see org.springframework.context.annotation.Configuration
 * @see io.swagger.v3.oas.models.OpenAPI
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures and provides a custom OpenAPI bean for the application.
     * <p>
     * This method sets up the API documentation
     * with metadata (title, version, description)
     * and defines the server URLs where the API is accessible.
     *
     * @return an {@link OpenAPI} instance configured for the application
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("URL Shortner API")
                        .version("1.0")
                        .description("Documentation version 1 of the API"))
                .servers(List.of(
                        new Server().url("http://localhost:8081/")
                                .description("Local Server")
                ));
    }
}
