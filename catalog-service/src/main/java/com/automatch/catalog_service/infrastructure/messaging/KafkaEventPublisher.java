package com.automatch.catalog_service.infrastructure.messaging;

import com.automatch.catalog_service.application.service.EventPublisher;
import com.automatch.catalog_service.domain.event.ProfessionalUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.scheduling.annotation.Async;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String CATALOG_EVENTS_TOPIC = "topic-catalog-events";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProfessionalUpdatedEvent(ProfessionalUpdatedEvent event) {
        log.info("Transaction committed. Publishing event to Kafka: {}", event);
        kafkaTemplate.send(CATALOG_EVENTS_TOPIC, event.getId().toString(), event);
    }
}
