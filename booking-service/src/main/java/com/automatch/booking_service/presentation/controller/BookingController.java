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
@Tag(name = "Agendamentos", description = "Endpoints para solicitações de serviços")
public class BookingController {
    private final CreateBookingUseCase createBookingUseCase;

    @PostMapping
    @Operation(summary = "Criar um agendamento", description = "Solicita um novo agendamento de serviço com um profissional")
    public ResponseEntity<Booking> create(@Valid @RequestBody CreateBookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createBookingUseCase.execute(request));
    }
}
