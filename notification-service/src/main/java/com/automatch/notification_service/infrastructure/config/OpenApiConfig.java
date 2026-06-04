package com.automatch.notification_service.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI notificationServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API do Serviço de Notificação do AutoMatch")
                        .description("Serviço responsável pelo envio de notificações para clientes e profissionais")
                        .version("1.0.0"));
    }
}
