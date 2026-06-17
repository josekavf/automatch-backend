package com.automatch.booking_service.infrastructure.persistence;

import com.automatch.booking_service.domain.model.Booking;
import com.automatch.booking_service.domain.repository.BookingRepository;
import com.automatch.booking_service.infrastructure.persistence.entity.BookingEntity;
import com.automatch.booking_service.infrastructure.persistence.repository.JpaBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {
    private final JpaBookingRepository jpaBookingRepository;

    @Override
    public Booking save(Booking booking) {
        BookingEntity entity = toEntity(booking);
        BookingEntity saved = jpaBookingRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        return jpaBookingRepository.findById(id).map(this::toDomain);
    }

    private BookingEntity toEntity(Booking b) {
        return BookingEntity.builder()
                .id(b.getId())
                .clientId(b.getClientId())
                .professionalId(b.getProfessionalId())
                .serviceName(b.getServiceName())
                .appointmentTime(b.getAppointmentTime())
                .status(b.getStatus())
                .build();
    }

    private Booking toDomain(BookingEntity e) {
        return Booking.builder()
                .id(e.getId())
                .clientId(e.getClientId())
                .professionalId(e.getProfessionalId())
                .serviceName(e.getServiceName())
                .appointmentTime(e.getAppointmentTime())
                .status(e.getStatus())
                .build();
    }

    @Override
    public org.springframework.data.domain.Page<Booking> findAll(com.automatch.booking_service.domain.model.BookingStatus status, UUID clientId, UUID professionalId, org.springframework.data.domain.Pageable pageable) {
        return jpaBookingRepository.findBookings(status, clientId, professionalId, pageable).map(this::toDomain);
    }
}
