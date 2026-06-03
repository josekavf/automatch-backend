package com.automatch.iam_service.infrastructure.messaging;

import com.automatch.iam_service.application.service.EventPublisher;
import com.automatch.iam_service.domain.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String USER_EVENTS_TOPIC = "topic-user-events";

    @Override
    public void publishUserRegistered(UserRegisteredEvent event) {
        log.info("Publishing user registered event: {}", event);
        kafkaTemplate.send(USER_EVENTS_TOPIC, event.getUserId().toString(), event);
    }
}
