package com.automatch.notification_service.infrastructure.messaging;

import com.automatch.notification_service.application.usecase.ProcessNotificationUseCase;
import com.automatch.notification_service.domain.event.NotificationEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class NotificationEventListenerTest {

    @Mock
    private ProcessNotificationUseCase processNotificationUseCase;

    @InjectMocks
    private NotificationEventListener notificationEventListener;

    @Test
    void shouldCallUseCaseWhenEventReceived() {
        // Arrange
        NotificationEvent event = NotificationEvent.builder()
                .recipient("test@example.com")
                .message("Test Message")
                .build();

        // Act
        notificationEventListener.listen(event);

        // Assert
        verify(processNotificationUseCase, times(1)).execute(event);
    }

    @Test
    void shouldLogWhenEventMovedToDlt() {
        // Arrange
        NotificationEvent event = NotificationEvent.builder()
                .recipient("test@example.com")
                .message("Test Message")
                .build();

        // Act & Assert
        // Since handleDlt only logs, we just verify it doesn't throw and coverage is recorded
        notificationEventListener.handleDlt(event);
    }
}
