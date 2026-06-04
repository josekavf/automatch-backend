package com.automatch.booking_service.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bookingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API do Serviço de Agendamento do AutoMatch")
                        .description("Serviço de Agendamento e Gestão de OS para a plataforma AutoMatch")
                        .version("1.0.0"));
    }
}
