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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateBookingUseCaseTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private CreateBookingUseCase createBookingUseCase;

    @Test
    void execute_ShouldSaveBookingAndPublishEvent() {
        // Arrange
        CreateBookingRequest request = new CreateBookingRequest();
        request.setClientId(UUID.randomUUID());
        request.setProfessionalId(UUID.randomUUID());
        request.setServiceName("Oil Change");
        request.setAppointmentTime(LocalDateTime.now().plusDays(1));

        Booking savedBooking = Booking.builder()
                .id(UUID.randomUUID())
                .clientId(request.getClientId())
                .professionalId(request.getProfessionalId())
                .serviceName(request.getServiceName())
                .appointmentTime(request.getAppointmentTime())
                .status(BookingStatus.REQUESTED)
                .build();

        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        // Act
        Booking result = createBookingUseCase.execute(request);

        // Assert
        assertNotNull(result);
        assertEquals(BookingStatus.REQUESTED, result.getStatus());
        assertEquals(request.getClientId(), result.getClientId());
        
        verify(bookingRepository).save(any(Booking.class));
        verify(eventPublisher).publishBookingRequested(any());
    }
}
