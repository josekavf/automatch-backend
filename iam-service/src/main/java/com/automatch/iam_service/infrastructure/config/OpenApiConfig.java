package com.automatch.iam_service.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI iamOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AutoMatch IAM Service API")
                        .description("Identity and Access Management service for AutoMatch platform")
                        .version("1.0.0"));
    }
}
