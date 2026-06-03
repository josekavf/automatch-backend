package com.automatch.booking_service.infrastructure.persistence.repository;

import com.automatch.booking_service.infrastructure.persistence.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface JpaBookingRepository extends JpaRepository<BookingEntity, UUID> {
}
