package com.automatch.catalog_service.application.usecase;

import com.automatch.catalog_service.infrastructure.cache.ProfessionalRedisEntity;
import com.automatch.catalog_service.infrastructure.cache.ProfessionalRedisRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchProfessionalUseCaseTest {

    @Mock
    private ProfessionalRedisRepository professionalRedisRepository;

    @InjectMocks
    private SearchProfessionalUseCase searchProfessionalUseCase;

    @Test
    void execute_WhenSpecialtyProvided_ShouldFilterBySpecialty() {
        // Preparação
        String specialty = "Mechanic";
        ProfessionalRedisEntity entity = ProfessionalRedisEntity.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .specialty(specialty)
                .build();
        
        when(professionalRedisRepository.findBySpecialty(specialty)).thenReturn(List.of(entity));

        // Execução
        List<ProfessionalRedisEntity> result = searchProfessionalUseCase.execute(specialty);

        // Verificação
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(specialty, result.get(0).getSpecialty());
        verify(professionalRedisRepository).findBySpecialty(specialty);
    }

    @Test
    void execute_WhenNoSpecialtyProvided_ShouldReturnAll() {
        // Preparação
        ProfessionalRedisEntity entity = ProfessionalRedisEntity.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .build();
        
        when(professionalRedisRepository.findAll()).thenReturn(List.of(entity));

        // Execução
        List<ProfessionalRedisEntity> result = searchProfessionalUseCase.execute(null);

        // Verificação
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(professionalRedisRepository).findAll();
    }
}
