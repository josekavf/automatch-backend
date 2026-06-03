package com.automatch.catalog_service.application.usecase;

import com.automatch.catalog_service.domain.event.ProfessionalUpdatedEvent;
import com.automatch.catalog_service.infrastructure.cache.ProfessionalRedisEntity;
import com.automatch.catalog_service.infrastructure.cache.ProfessionalRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProfessionalProjectionUseCase {
    private final ProfessionalRedisRepository professionalRedisRepository;

    public void execute(ProfessionalUpdatedEvent event) {
        ProfessionalRedisEntity entity = ProfessionalRedisEntity.builder()
                .id(event.getId())
                .firstName(event.getFirstName())
                .lastName(event.getLastName())
                .specialty(event.getSpecialty())
                .services(event.getServices())
                .active(event.isActive())
                .build();
        professionalRedisRepository.save(entity);
    }
}
