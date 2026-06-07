package com.automatch.booking_service.infrastructure.persistence;

import com.automatch.booking_service.domain.model.Booking;
import com.automatch.booking_service.domain.model.BookingStatus;
import com.automatch.booking_service.infrastructure.persistence.entity.BookingEntity;
import com.automatch.booking_service.infrastructure.persistence.repository.JpaBookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingRepositoryImplTest {

    @Mock
    private JpaBookingRepository jpaBookingRepository;

    @InjectMocks
    private BookingRepositoryImpl bookingRepository;

    private Booking booking;
    private BookingEntity bookingEntity;
    private UUID bookingId;

    @BeforeEach
    void setUp() {
        bookingId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID professionalId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        booking = Booking.builder()
                .id(bookingId)
                .clientId(clientId)
                .professionalId(professionalId)
                .serviceName("Test Service")
                .appointmentTime(now)
                .status(BookingStatus.REQUESTED)
                .build();

        bookingEntity = BookingEntity.builder()
                .id(bookingId)
                .clientId(clientId)
                .professionalId(professionalId)
                .serviceName("Test Service")
                .appointmentTime(now)
                .status(BookingStatus.REQUESTED)
                .build();
    }

    @Test
    void shouldSaveBooking() {
        when(jpaBookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);

        Booking saved = bookingRepository.save(booking);

        assertNotNull(saved);
        assertEquals(bookingId, saved.getId());
        assertEquals(booking.getClientId(), saved.getClientId());
        assertEquals(booking.getServiceName(), saved.getServiceName());
        verify(jpaBookingRepository, times(1)).save(any(BookingEntity.class));
    }

    @Test
    void shouldFindBookingById() {
        when(jpaBookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingEntity));

        Optional<Booking> found = bookingRepository.findById(bookingId);

        assertTrue(found.isPresent());
        assertEquals(bookingId, found.get().getId());
        verify(jpaBookingRepository, times(1)).findById(bookingId);
    }

    @Test
    void shouldReturnEmptyWhenBookingNotFound() {
        when(jpaBookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        Optional<Booking> found = bookingRepository.findById(bookingId);

        assertFalse(found.isPresent());
        verify(jpaBookingRepository, times(1)).findById(bookingId);
    }
}
