package com.automatch.booking_service.domain.repository;

import com.automatch.booking_service.domain.model.Booking;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository {
    Booking save(Booking booking);
    Optional<Booking> findById(UUID id);
    org.springframework.data.domain.Page<Booking> findAll(com.automatch.booking_service.domain.model.BookingStatus status, UUID clientId, UUID professionalId, org.springframework.data.domain.Pageable pageable);
}
