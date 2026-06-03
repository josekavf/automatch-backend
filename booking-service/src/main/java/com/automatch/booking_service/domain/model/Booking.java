package com.automatch.booking_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private UUID id;
    private UUID clientId;
    private UUID professionalId;
    private String serviceName;
    private LocalDateTime appointmentTime;
    private BookingStatus status;
}
