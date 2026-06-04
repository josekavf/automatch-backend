package com.automatch.notification_service.application.usecase;

import com.automatch.notification_service.domain.event.NotificationEvent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ProcessNotificationUseCaseTest {

    @Test
    void shouldProcessNotificationWithoutErrors() {
        ProcessNotificationUseCase useCase = new ProcessNotificationUseCase();
        NotificationEvent event = NotificationEvent.builder()
                .recipient("test@example.com")
                .message("Hello Test")
                .build();

        assertDoesNotThrow(() -> useCase.execute(event));
    }
}
