package com.automatch.booking_service.application.usecase;

import com.automatch.booking_service.domain.model.Booking;
import com.automatch.booking_service.domain.model.BookingStatus;
import com.automatch.booking_service.domain.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListBookingsUseCase {
    private final BookingRepository bookingRepository;

    public Page<Booking> execute(BookingStatus status, UUID clientId, UUID professionalId, Pageable pageable) {
        return bookingRepository.findAll(status, clientId, professionalId, pageable);
    }
}
