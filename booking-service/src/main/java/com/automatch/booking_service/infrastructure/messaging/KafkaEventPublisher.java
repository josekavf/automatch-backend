package com.automatch.booking_service.infrastructure.messaging;

import com.automatch.booking_service.application.service.EventPublisher;
import com.automatch.booking_service.domain.event.BookingRequestedEvent;
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

    @Override
    public void publishBookingRequested(BookingRequestedEvent event) {
        log.info("Publishing booking requested event: {}", event);
        kafkaTemplate.send(BOOKING_EVENTS_TOPIC, event.getBookingId().toString(), event);
    }
}
