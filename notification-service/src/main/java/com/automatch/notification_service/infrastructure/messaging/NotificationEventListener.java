package com.automatch.notification_service.infrastructure.messaging;

import com.automatch.notification_service.application.usecase.ProcessNotificationUseCase;
import com.automatch.notification_service.domain.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final ProcessNotificationUseCase processNotificationUseCase;

    @KafkaListener(topics = "topic-notification-events", groupId = "notification-service-group")
    public void listen(NotificationEvent event) {
        log.info("Received notification event for recipient: {}", event.getRecipient());
        processNotificationUseCase.execute(event);
    }
}
