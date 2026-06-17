package com.automatch.iam_service.infrastructure.messaging;

import com.automatch.iam_service.application.service.EventPublisher;
import com.automatch.iam_service.domain.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringEventPublisherImpl implements EventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishUserRegistered(UserRegisteredEvent event) {
        // Publishes to Spring context. Listeners will handle Kafka delivery.
        applicationEventPublisher.publishEvent(event);
    }
}
