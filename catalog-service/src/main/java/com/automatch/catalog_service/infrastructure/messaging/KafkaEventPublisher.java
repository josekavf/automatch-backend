package com.automatch.catalog_service.infrastructure.messaging;

import com.automatch.catalog_service.application.service.EventPublisher;
import com.automatch.catalog_service.domain.event.ProfessionalUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String CATALOG_EVENTS_TOPIC = "topic-catalog-events";

    @Override
    public void publishProfessionalUpdated(ProfessionalUpdatedEvent event) {
        log.info("Publicando evento de profissional atualizado: {}", event);
        kafkaTemplate.send(CATALOG_EVENTS_TOPIC, event.getId().toString(), event);
    }
}
