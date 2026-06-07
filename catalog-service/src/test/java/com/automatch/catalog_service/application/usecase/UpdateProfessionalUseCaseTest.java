package com.automatch.catalog_service.application.usecase;

import com.automatch.catalog_service.application.dto.UpdateProfessionalRequest;
import com.automatch.catalog_service.application.service.EventPublisher;
import com.automatch.catalog_service.domain.event.ProfessionalUpdatedEvent;
import com.automatch.catalog_service.domain.model.Professional;
import com.automatch.catalog_service.domain.repository.ProfessionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProfessionalUseCaseTest {

    @Mock
    private ProfessionalRepository professionalRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private UpdateProfessionalUseCase updateProfessionalUseCase;

    private UUID id;
    private UpdateProfessionalRequest request;
    private Professional professional;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        request = UpdateProfessionalRequest.builder()
                .firstName("Jane")
                .lastName("Smith")
                .specialty("Electrician")
                .services(List.of("Wiring", "Lighting"))
                .active(true)
                .build();

        professional = Professional.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .specialty("Mechanic")
                .services(List.of("Oil change"))
                .active(false)
                .build();
    }

    @Test
    void shouldUpdateProfessionalSuccessfully() {
        when(professionalRepository.findById(id)).thenReturn(Optional.of(professional));

        updateProfessionalUseCase.execute(id, request);

        verify(professionalRepository).save(professional);
        verify(eventPublisher).publishProfessionalUpdated(any(ProfessionalUpdatedEvent.class));
    }

    @Test
    void shouldThrowExceptionWhenProfessionalNotFound() {
        when(professionalRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateProfessionalUseCase.execute(id, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Profissional não encontrado");

        verify(professionalRepository, never()).save(any());
        verify(eventPublisher, never()).publishProfessionalUpdated(any());
    }
}
