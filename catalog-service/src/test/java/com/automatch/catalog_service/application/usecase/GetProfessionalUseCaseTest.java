package com.automatch.catalog_service.application.usecase;

import com.automatch.catalog_service.domain.model.Professional;
import com.automatch.catalog_service.domain.repository.ProfessionalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetProfessionalUseCaseTest {

    @Mock
    private ProfessionalRepository professionalRepository;

    @InjectMocks
    private GetProfessionalUseCase getProfessionalUseCase;

    @Test
    void execute_WhenProfessionalExists_ShouldReturnProfessional() {
        UUID id = UUID.randomUUID();
        Professional professional = Professional.builder()
                .id(id)
                .firstName("John")
                .build();

        when(professionalRepository.findById(id)).thenReturn(Optional.of(professional));

        Professional result = getProfessionalUseCase.execute(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("John", result.getFirstName());
    }

    @Test
    void execute_WhenProfessionalNotFound_ShouldThrowException() {
        UUID id = UUID.randomUUID();
        when(professionalRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> getProfessionalUseCase.execute(id));
        assertEquals("Profissional não encontrado", exception.getMessage());
    }
}
