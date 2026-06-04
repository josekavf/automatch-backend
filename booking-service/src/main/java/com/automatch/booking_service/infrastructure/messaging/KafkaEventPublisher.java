package com.automatch.booking_service.infrastructure.messaging;

import com.automatch.booking_service.application.service.EventPublisher;
import com.automatch.booking_service.domain.event.BookingRequestedEvent;
import com.automatch.booking_service.domain.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String BOOKING_EVENTS_TOPIC = "topic-booking-events";
    private static final String NOTIFICATION_EVENTS_TOPIC = "topic-notification-events";

    @Override
    public void publishBookingRequested(BookingRequestedEvent event) {
        log.info("Publishing booking requested event: {}", event);
        kafkaTemplate.send(BOOKING_EVENTS_TOPIC, event.getBookingId().toString(), event);
    }

    @Override
    public void publishNotification(NotificationEvent event) {
        log.info("Publishing notification event: {}", event);
        kafkaTemplate.send(NOTIFICATION_EVENTS_TOPIC, event.getEmail(), event);
    }
}
