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
                        .title("AutoMatch Booking Service API")
                        .description("Service Booking and OS Management for AutoMatch platform")
                        .version("1.0.0"));
    }
}
