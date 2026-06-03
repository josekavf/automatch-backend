package com.automatch.booking_service.presentation.controller;

import com.automatch.booking_service.application.dto.CreateBookingRequest;
import com.automatch.booking_service.application.usecase.CreateBookingUseCase;
import com.automatch.booking_service.domain.model.Booking;
import com.automatch.booking_service.domain.model.BookingStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateBookingUseCase createBookingUseCase;

    @Test
    void create_ShouldReturnCreated() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest();
        request.setClientId(UUID.randomUUID());
        request.setProfessionalId(UUID.randomUUID());
        request.setServiceName("Oil Change");
        request.setAppointmentTime(LocalDateTime.now().plusDays(1));

        Booking booking = Booking.builder()
                .id(UUID.randomUUID())
                .status(BookingStatus.REQUESTED)
                .build();

        when(createBookingUseCase.execute(any())).thenReturn(booking);

        mockMvc.perform(post("/api/v1/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("REQUESTED"));
    }
}
