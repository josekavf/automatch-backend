package com.automatch.booking_service.application.usecase;

import com.automatch.booking_service.application.dto.UpdateBookingStatusRequest;
import com.automatch.booking_service.application.service.EventPublisher;
import com.automatch.booking_service.domain.event.NotificationEvent;
import com.automatch.booking_service.domain.model.Booking;
import com.automatch.booking_service.domain.model.BookingStatus;
import com.automatch.booking_service.domain.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateBookingStatusUseCase {
    private final BookingRepository bookingRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Booking execute(UUID bookingId, UpdateBookingStatusRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));

        booking.setStatus(request.getStatus());
        Booking updatedBooking = bookingRepository.save(booking);

        String statusMessage = "";
        if (request.getStatus() == BookingStatus.APPROVED) {
            statusMessage = "aprovado";
        } else if (request.getStatus() == BookingStatus.REJECTED) {
            statusMessage = "recusado";
        } else {
            statusMessage = "atualizado para " + request.getStatus().name();
        }

        eventPublisher.publishNotification(NotificationEvent.builder()
                .recipient(request.getClientEmail())
                .message(String.format("Seu agendamento para %s foi %s pelo profissional.",
                        booking.getServiceName(), statusMessage))
                .build());

        return updatedBooking;
    }
}
