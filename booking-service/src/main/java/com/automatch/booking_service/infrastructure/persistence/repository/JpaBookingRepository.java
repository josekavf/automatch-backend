package com.automatch.booking_service.infrastructure.persistence.repository;

import com.automatch.booking_service.infrastructure.persistence.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.automatch.booking_service.domain.model.BookingStatus;

public interface JpaBookingRepository extends JpaRepository<BookingEntity, UUID> {
    
    @Query("SELECT b FROM BookingEntity b WHERE " +
           "(:status IS NULL OR b.status = :status) AND " +
           "(CAST(:clientId AS org.hibernate.type.UUIDCharType) IS NULL OR b.clientId = :clientId) AND " +
           "(CAST(:professionalId AS org.hibernate.type.UUIDCharType) IS NULL OR b.professionalId = :professionalId)")
    Page<BookingEntity> findBookings(
        @Param("status") BookingStatus status,
        @Param("clientId") UUID clientId,
        @Param("professionalId") UUID professionalId,
        Pageable pageable
    );
}
