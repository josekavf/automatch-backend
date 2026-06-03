package com.automatch.booking_service.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateBookingRequest {
    @NotNull
    private UUID clientId;
    @NotNull
    private UUID professionalId;
    @NotBlank
    private String serviceName;
    @NotNull
    private LocalDateTime appointmentTime;
}
