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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProfessionalUseCaseTest {

    @Mock
    private ProfessionalRepository professionalRepository;

    @Mock
    private org.springframework.context.ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private UpdateProfessionalUseCase updateProfessionalUseCase;

    private UUID professionalId;
    private UpdateProfessionalRequest request;
    private Professional existingProfessional;

    @BeforeEach
    void setUp() {
        professionalId = UUID.randomUUID();
        
        request = new UpdateProfessionalRequest();
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setSpecialty("Electrician");
        request.setServices(List.of("Battery Replacement"));
        request.setActive(false);

        existingProfessional = Professional.builder()
                .id(professionalId)
                .firstName("John")
                .lastName("Doe")
                .specialty("Mechanic")
                .services(List.of("Oil Change"))
                .active(true)
                .build();
    }

    @Test
    void execute_WhenProfessionalExists_ShouldUpdateAndPublishEvent() {
        when(professionalRepository.findById(professionalId)).thenReturn(Optional.of(existingProfessional));
        when(professionalRepository.save(any(Professional.class))).thenReturn(existingProfessional);

        updateProfessionalUseCase.execute(professionalId, request);

        assertEquals(request.getFirstName(), existingProfessional.getFirstName());
        assertEquals(request.getLastName(), existingProfessional.getLastName());
        assertEquals(request.getSpecialty(), existingProfessional.getSpecialty());
        assertEquals(request.getServices(), existingProfessional.getServices());
        assertEquals(request.isActive(), existingProfessional.isActive());

        verify(professionalRepository).save(existingProfessional);
        verify(applicationEventPublisher).publishEvent(any(com.automatch.catalog_service.domain.event.ProfessionalUpdatedEvent.class));
    }

    @Test
    void shouldThrowExceptionWhenProfessionalNotFound() {
        when(professionalRepository.findById(professionalId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateProfessionalUseCase.execute(professionalId, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Profissional não encontrado");

        verify(professionalRepository, never()).save(any());
        verify(applicationEventPublisher, never()).publishEvent(any());
    }
}
