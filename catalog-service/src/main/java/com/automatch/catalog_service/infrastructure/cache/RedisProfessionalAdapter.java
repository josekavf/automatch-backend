package com.automatch.catalog_service.infrastructure.cache;

import com.automatch.catalog_service.domain.model.Professional;
import com.automatch.catalog_service.domain.repository.ProfessionalReadModelPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisProfessionalAdapter implements ProfessionalReadModelPort {
    private final ProfessionalRedisRepository professionalRedisRepository;

    @Override
    public void saveReadModel(Professional professional) {
        ProfessionalRedisEntity entity = ProfessionalRedisEntity.builder()
                .id(professional.getId())
                .firstName(professional.getFirstName())
                .lastName(professional.getLastName())
                .email(professional.getEmail())
                .active(professional.isActive())
                .build();
        professionalRedisRepository.save(entity);
    }
}
