package com.automatch.booking_service.application.usecase;

import com.automatch.booking_service.application.dto.CreateBookingRequest;
import com.automatch.booking_service.application.service.EventPublisher;
import com.automatch.booking_service.domain.model.Booking;
import com.automatch.booking_service.domain.model.BookingStatus;
import com.automatch.booking_service.domain.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBookingUseCaseTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private CreateBookingUseCase createBookingUseCase;

    @Test
    void execute_ShouldSaveBookingAndPublishEvents() {
        // Preparação
        CreateBookingRequest request = new CreateBookingRequest();
        request.setClientId(UUID.randomUUID());
        request.setClientEmail("client@example.com");
        request.setProfessionalId(UUID.randomUUID());
        request.setProfessionalEmail("pro@example.com");
        request.setServiceName("Oil Change");
        LocalDateTime time = LocalDateTime.of(2026, 6, 10, 10, 0);
        request.setAppointmentTime(time);

        Booking savedBooking = Booking.builder()
                .id(UUID.randomUUID())
                .clientId(request.getClientId())
                .professionalId(request.getProfessionalId())
                .serviceName(request.getServiceName())
                .appointmentTime(request.getAppointmentTime())
                .status(BookingStatus.REQUESTED)
                .build();

        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        // Execução
        Booking result = createBookingUseCase.execute(request);

        // Verificação
        assertNotNull(result);
        assertEquals(BookingStatus.REQUESTED, result.getStatus());
        
        verify(bookingRepository).save(any(Booking.class));
        verify(eventPublisher).publishBookingRequested(any());
        
        // Verify notifications
        verify(eventPublisher, atLeastOnce()).publishNotification(argThat(event -> 
            event.getRecipient().equals("client@example.com") && 
            event.getMessage().contains("Você solicitou um agendamento para Oil Change")
        ));
        
        verify(eventPublisher, atLeastOnce()).publishNotification(argThat(event -> 
            event.getRecipient().equals("pro@example.com") && 
            event.getMessage().contains("Você recebeu uma nova solicitação de agendamento para Oil Change")
        ));
    }
}
