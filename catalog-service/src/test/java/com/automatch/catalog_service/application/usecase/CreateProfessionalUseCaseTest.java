package com.automatch.catalog_service.application.usecase;

import com.automatch.catalog_service.domain.event.UserRegisteredEvent;
import com.automatch.catalog_service.domain.model.Professional;
import com.automatch.catalog_service.domain.repository.ProfessionalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateProfessionalUseCaseTest {

    @Mock
    private ProfessionalRepository professionalRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private CreateProfessionalUseCase createProfessionalUseCase;

    @Test
    void execute_ShouldSaveProfessional() {
        UserRegisteredEvent event = new UserRegisteredEvent(UUID.randomUUID(), "john.doe@example.com", "John", "Doe", "PROFESSIONAL");

        createProfessionalUseCase.execute(event);

        verify(professionalRepository).save(any(Professional.class));
        verify(applicationEventPublisher).publishEvent(any(com.automatch.catalog_service.domain.event.ProfessionalCreatedEvent.class));
    }
}
