package com.automatch.booking_service.application.usecase;

import com.automatch.booking_service.application.dto.CreateBookingRequest;
import com.automatch.booking_service.application.service.EventPublisher;
import com.automatch.booking_service.domain.event.BookingRequestedEvent;
import com.automatch.booking_service.domain.model.Booking;
import com.automatch.booking_service.domain.model.BookingStatus;
import com.automatch.booking_service.domain.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateBookingUseCase {
    private final BookingRepository bookingRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Booking execute(CreateBookingRequest request) {
        Booking booking = Booking.builder()
                .clientId(request.getClientId())
                .professionalId(request.getProfessionalId())
                .serviceName(request.getServiceName())
                .appointmentTime(request.getAppointmentTime())
                .status(BookingStatus.REQUESTED)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        eventPublisher.publishBookingRequested(BookingRequestedEvent.builder()
                .bookingId(savedBooking.getId())
                .clientId(savedBooking.getClientId())
                .professionalId(savedBooking.getProfessionalId())
                .serviceName(savedBooking.getServiceName())
                .appointmentTime(savedBooking.getAppointmentTime())
                .build());

        return savedBooking;
    }
}
