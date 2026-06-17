package com.automatch.booking_service.application.dto;

import com.automatch.booking_service.domain.model.BookingStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateBookingStatusRequest {
    @NotNull
    private BookingStatus status;

    @NotBlank
    @Email
    private String clientEmail;
}
