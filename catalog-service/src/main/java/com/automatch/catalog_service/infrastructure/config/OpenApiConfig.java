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
                        .title("API do Serviço de Catálogo do AutoMatch")
                        .description("Serviço de Catálogo de Profissionais para a plataforma AutoMatch")
                        .version("1.0.0"));
    }
}
