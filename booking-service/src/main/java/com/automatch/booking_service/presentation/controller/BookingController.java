package com.automatch.booking_service.presentation.controller;

import com.automatch.booking_service.application.dto.CreateBookingRequest;
import com.automatch.booking_service.application.usecase.CreateBookingUseCase;
import com.automatch.booking_service.domain.model.Booking;
import com.automatch.booking_service.presentation.annotation.Idempotent;
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

import com.automatch.booking_service.application.dto.UpdateBookingStatusRequest;
import com.automatch.booking_service.application.usecase.UpdateBookingStatusUseCase;
import com.automatch.booking_service.application.usecase.ListBookingsUseCase;
import com.automatch.booking_service.domain.model.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "Agendamentos", description = "Endpoints para solicitações de serviços")
public class BookingController {
    private final CreateBookingUseCase createBookingUseCase;
    private final UpdateBookingStatusUseCase updateBookingStatusUseCase;
    private final ListBookingsUseCase listBookingsUseCase;

    @Idempotent
    @PostMapping
    @Operation(summary = "Criar um agendamento", description = "Solicita um novo agendamento de serviço com um profissional")
    public ResponseEntity<Booking> create(@Valid @RequestBody CreateBookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createBookingUseCase.execute(request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do agendamento", description = "Atualiza o status de um agendamento (ex: PENDING para APPROVED ou REJECTED)")
    public ResponseEntity<Booking> updateStatus(@PathVariable UUID id, @Valid @RequestBody UpdateBookingStatusRequest request) {
        return ResponseEntity.ok(updateBookingStatusUseCase.execute(id, request));
    }

    @GetMapping
    @Operation(summary = "Listar agendamentos", description = "Lista todos os agendamentos paginados com filtros opcionais")
    public ResponseEntity<Page<Booking>> listBookings(
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) UUID clientId,
            @RequestParam(required = false) UUID professionalId,
            Pageable pageable) {
        return ResponseEntity.ok(listBookingsUseCase.execute(status, clientId, professionalId, pageable));
    }
}
