package com.automatch.notification_service.infrastructure.messaging;

import com.automatch.notification_service.application.usecase.ProcessNotificationUseCase;
import com.automatch.notification_service.domain.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final ProcessNotificationUseCase processNotificationUseCase;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            autoCreateTopics = "true",
            dltTopicSuffix = "-dlt"
    )
    @KafkaListener(topics = "topic-notification-events", groupId = "notification-service-group")
    public void listen(NotificationEvent event) {
        log.info("Received notification event for recipient: {}", event.getRecipient());
        processNotificationUseCase.execute(event);
    }

    @DltHandler
    public void handleDlt(NotificationEvent event) {
        log.error("Event failed permanently and moved to DLQ: {}", event);
    }
}
