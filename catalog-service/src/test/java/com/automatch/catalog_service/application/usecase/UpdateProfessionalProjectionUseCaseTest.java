package com.automatch.catalog_service.application.usecase;

import com.automatch.catalog_service.domain.event.ProfessionalUpdatedEvent;
import com.automatch.catalog_service.infrastructure.cache.ProfessionalRedisEntity;
import com.automatch.catalog_service.infrastructure.cache.ProfessionalRedisRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateProfessionalProjectionUseCaseTest {

    @Mock
    private ProfessionalRedisRepository professionalRedisRepository;

    @InjectMocks
    private UpdateProfessionalProjectionUseCase updateProfessionalProjectionUseCase;

    @Test
    void shouldUpdateProjection() {
        ProfessionalUpdatedEvent event = ProfessionalUpdatedEvent.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .specialty("Mechanic")
                .services(List.of("Oil change"))
                .active(true)
                .build();

        updateProfessionalProjectionUseCase.execute(event);

        verify(professionalRedisRepository).save(any(ProfessionalRedisEntity.class));
    }
}
