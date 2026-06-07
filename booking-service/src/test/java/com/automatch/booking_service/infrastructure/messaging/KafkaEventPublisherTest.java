package com.automatch.booking_service.infrastructure.messaging;

import com.automatch.booking_service.domain.event.BookingRequestedEvent;
import com.automatch.booking_service.domain.event.NotificationEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaEventPublisherTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private KafkaEventPublisher kafkaEventPublisher;

    @Test
    void shouldPublishBookingRequestedEvent() {
        UUID bookingId = UUID.randomUUID();
        BookingRequestedEvent event = BookingRequestedEvent.builder()
                .bookingId(bookingId)
                .clientId(UUID.randomUUID())
                .professionalId(UUID.randomUUID())
                .serviceName("Test Service")
                .appointmentTime(LocalDateTime.now())
                .build();

        kafkaEventPublisher.publishBookingRequested(event);

        verify(kafkaTemplate, times(1)).send(eq("topic-booking-events"), eq(bookingId.toString()), eq(event));
    }

    @Test
    void shouldPublishNotificationEvent() {
        NotificationEvent event = NotificationEvent.builder()
                .recipient("test@example.com")
                .message("Test message")
                .build();

        kafkaEventPublisher.publishNotification(event);

        verify(kafkaTemplate, times(1)).send(eq("topic-notification-events"), eq("test@example.com"), eq(event));
    }
}
