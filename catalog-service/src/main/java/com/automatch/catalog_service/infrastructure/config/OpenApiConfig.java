package com.automatch.catalog_service.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI catalogOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AutoMatch Catalog Service API")
                        .description("Professional Catalog service for AutoMatch platform")
                        .version("1.0.0"));
    }
}
