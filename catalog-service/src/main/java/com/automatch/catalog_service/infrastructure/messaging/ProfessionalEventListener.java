package com.automatch.catalog_service.infrastructure.messaging;

import com.automatch.catalog_service.application.usecase.CreateProfessionalUseCase;
import com.automatch.catalog_service.application.usecase.UpdateProfessionalProjectionUseCase;
import com.automatch.catalog_service.domain.event.ProfessionalUpdatedEvent;
import com.automatch.catalog_service.domain.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfessionalEventListener {
    private final CreateProfessionalUseCase createProfessionalUseCase;
    private final UpdateProfessionalProjectionUseCase updateProfessionalProjectionUseCase;

    @KafkaListener(topics = "topic-user-events", groupId = "catalog-service-group")
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("Received user registered event: {}", event);
        if ("MECHANIC".equals(event.getRole())) {
            createProfessionalUseCase.execute(event);
        }
    }

    @KafkaListener(topics = "topic-catalog-events", groupId = "catalog-service-group")
    public void handleProfessionalUpdated(ProfessionalUpdatedEvent event) {
        log.info("Received professional updated event: {}", event);
        updateProfessionalProjectionUseCase.execute(event);
    }
}
