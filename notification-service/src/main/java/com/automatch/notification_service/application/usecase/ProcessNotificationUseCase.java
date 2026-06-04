package com.automatch.notification_service.application.usecase;

import com.automatch.notification_service.domain.event.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProcessNotificationUseCase {

    public void execute(NotificationEvent event) {
        log.info("Sending notification to: {} - Message: {}", event.getRecipient(), event.getMessage());
    }
}
