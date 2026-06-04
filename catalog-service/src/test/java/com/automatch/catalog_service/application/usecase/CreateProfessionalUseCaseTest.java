package com.automatch.catalog_service.application.usecase;

import com.automatch.catalog_service.domain.event.UserRegisteredEvent;
import com.automatch.catalog_service.domain.model.Professional;
import com.automatch.catalog_service.domain.repository.ProfessionalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateProfessionalUseCaseTest {

    @Mock
    private ProfessionalRepository professionalRepository;

    @InjectMocks
    private CreateProfessionalUseCase createProfessionalUseCase;

    @Test
    void execute_ShouldSaveProfessional() {
        // Preparação
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .role("MECHANIC")
                .build();
        
        when(professionalRepository.save(any(Professional.class))).thenReturn(new Professional());

        // Execução
        createProfessionalUseCase.execute(event);

        // Verificação
        verify(professionalRepository).save(any(Professional.class));
    }
}
