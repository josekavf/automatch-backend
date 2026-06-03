package com.automatch.booking_service.presentation.controller;

import com.automatch.booking_service.application.dto.CreateBookingRequest;
import com.automatch.booking_service.application.usecase.CreateBookingUseCase;
import com.automatch.booking_service.domain.model.Booking;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking", description = "Endpoints for service appointments")
public class BookingController {
    private final CreateBookingUseCase createBookingUseCase;

    @PostMapping
    @Operation(summary = "Create a booking", description = "Requests a new service appointment with a professional")
    public ResponseEntity<Booking> create(@Valid @RequestBody CreateBookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createBookingUseCase.execute(request));
    }
}
