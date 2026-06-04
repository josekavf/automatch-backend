package com.automatch.booking_service.application.usecase;

import com.automatch.booking_service.application.dto.CreateBookingRequest;
import com.automatch.booking_service.application.service.EventPublisher;
import com.automatch.booking_service.domain.event.BookingRequestedEvent;
import com.automatch.booking_service.domain.event.NotificationEvent;
import com.automatch.booking_service.domain.model.Booking;
import com.automatch.booking_service.domain.model.BookingStatus;
import com.automatch.booking_service.domain.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class CreateBookingUseCase {
    private final BookingRepository bookingRepository;
    private final EventPublisher eventPublisher;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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

        String formattedTime = savedBooking.getAppointmentTime().format(FORMATTER);

        // Notify Client
        eventPublisher.publishNotification(NotificationEvent.builder()
                .email(request.getClientEmail())
                .message(String.format("Você solicitou um agendamento para %s com o profissional em %s.",
                        savedBooking.getServiceName(), formattedTime))
                .build());

        // Notify Professional
        eventPublisher.publishNotification(NotificationEvent.builder()
                .email(request.getProfessionalEmail())
                .message(String.format("Você recebeu uma nova solicitação de agendamento para %s em %s.",
                        savedBooking.getServiceName(), formattedTime))
                .build());

        return savedBooking;
    }
}
