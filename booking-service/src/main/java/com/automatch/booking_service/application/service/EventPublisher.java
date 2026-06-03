package com.automatch.booking_service.application.service;

import com.automatch.booking_service.domain.event.BookingRequestedEvent;

public interface EventPublisher {
    void publishBookingRequested(BookingRequestedEvent event);
}
