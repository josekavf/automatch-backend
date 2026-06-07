package com.automatch.catalog_service.infrastructure.messaging;

import com.automatch.catalog_service.application.usecase.CreateProfessionalUseCase;
import com.automatch.catalog_service.application.usecase.UpdateProfessionalProjectionUseCase;
import com.automatch.catalog_service.domain.event.ProfessionalUpdatedEvent;
import com.automatch.catalog_service.domain.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfessionalEventListener {
    private final CreateProfessionalUseCase createProfessionalUseCase;
    private final UpdateProfessionalProjectionUseCase updateProfessionalProjectionUseCase;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            autoCreateTopics = "true",
            dltTopicSuffix = "-dlt"
    )
    @KafkaListener(topics = "topic-user-events", groupId = "catalog-service-group")
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("Received user registered event: {}", event);
        if ("MECHANIC".equals(event.getRole())) {
            createProfessionalUseCase.execute(event);
        }
    }

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            autoCreateTopics = "true",
            dltTopicSuffix = "-dlt"
    )
    @KafkaListener(topics = "topic-catalog-events", groupId = "catalog-service-group")
    public void handleProfessionalUpdated(ProfessionalUpdatedEvent event) {
        log.info("Received professional updated event: {}", event);
        updateProfessionalProjectionUseCase.execute(event);
    }

    @DltHandler
    public void handleUserRegisteredDlt(UserRegisteredEvent event) {
        log.error("User Registered event failed permanently and moved to DLQ: {}", event);
    }

    @DltHandler
    public void handleProfessionalUpdatedDlt(ProfessionalUpdatedEvent event) {
        log.error("Professional Updated event failed permanently and moved to DLQ: {}", event);
    }
}
