package com.automatch.catalog_service.infrastructure.messaging;

import com.automatch.catalog_service.application.usecase.CreateProfessionalUseCase;
import com.automatch.catalog_service.application.usecase.UpdateProfessionalProjectionUseCase;
import com.automatch.catalog_service.domain.event.ProfessionalUpdatedEvent;
import com.automatch.catalog_service.domain.event.UserRegisteredEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessionalEventListenerTest {

    @Mock
    private CreateProfessionalUseCase createProfessionalUseCase;

    @Mock
    private UpdateProfessionalProjectionUseCase updateProfessionalProjectionUseCase;

    @InjectMocks
    private ProfessionalEventListener professionalEventListener;

    @Test
    void shouldHandleUserRegisteredAsMechanic() {
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(UUID.randomUUID())
                .role("MECHANIC")
                .build();

        professionalEventListener.handleUserRegistered(event);

        verify(createProfessionalUseCase).execute(event);
    }

    @Test
    void shouldNotHandleUserRegisteredAsClient() {
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(UUID.randomUUID())
                .role("CLIENT")
                .build();

        professionalEventListener.handleUserRegistered(event);

        verify(createProfessionalUseCase, never()).execute(any());
    }

    @Test
    void shouldHandleProfessionalUpdated() {
        ProfessionalUpdatedEvent event = ProfessionalUpdatedEvent.builder()
                .id(UUID.randomUUID())
                .build();

        professionalEventListener.handleProfessionalUpdated(event);

        verify(updateProfessionalProjectionUseCase).execute(event);
    }

    @Test
    void shouldHandleUserRegisteredDlt() {
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(UUID.randomUUID())
                .build();

        professionalEventListener.handleUserRegisteredDlt(event);

        // Just verifies it doesn't throw and potentially logs (which we don't mock-verify here easily but the coverage is gained)
    }

    @Test
    void shouldHandleProfessionalUpdatedDlt() {
        ProfessionalUpdatedEvent event = ProfessionalUpdatedEvent.builder()
                .id(UUID.randomUUID())
                .build();

        professionalEventListener.handleProfessionalUpdatedDlt(event);
        // Just verifies it doesn't throw
    }
}
