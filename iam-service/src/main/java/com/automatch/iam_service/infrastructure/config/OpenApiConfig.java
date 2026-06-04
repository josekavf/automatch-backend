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
                        .title("API do Serviço IAM do AutoMatch")
                        .description("Serviço de Gestão de Identidade e Acesso para a plataforma AutoMatch")
                        .version("1.0.0"));
    }
}
