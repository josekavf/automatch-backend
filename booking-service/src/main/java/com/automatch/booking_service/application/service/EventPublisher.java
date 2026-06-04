package com.automatch.booking_service.application.service;

import com.automatch.booking_service.domain.event.BookingRequestedEvent;
import com.automatch.booking_service.domain.event.NotificationEvent;

public interface EventPublisher {
    void publishBookingRequested(BookingRequestedEvent event);
    void publishNotification(NotificationEvent event);
}
