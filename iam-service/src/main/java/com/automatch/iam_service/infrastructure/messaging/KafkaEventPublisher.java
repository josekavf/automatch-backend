package com.automatch.iam_service.infrastructure.messaging;

import com.automatch.iam_service.application.service.EventPublisher;
import com.automatch.iam_service.domain.event.UserRegisteredEvent;
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
    private static final String USER_EVENTS_TOPIC = "topic-user-events";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        log.info("Transaction committed. Publishing user registered event to Kafka: {}", event);
        kafkaTemplate.send(USER_EVENTS_TOPIC, event.getUserId().toString(), event);
    }
}
