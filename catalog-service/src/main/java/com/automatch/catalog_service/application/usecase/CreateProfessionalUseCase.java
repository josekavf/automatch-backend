package com.automatch.catalog_service.application.usecase;

import com.automatch.catalog_service.domain.event.UserRegisteredEvent;
import com.automatch.catalog_service.domain.model.Professional;
import com.automatch.catalog_service.domain.repository.ProfessionalRepository;
import com.automatch.catalog_service.infrastructure.cache.ProfessionalRedisEntity;
import com.automatch.catalog_service.infrastructure.cache.ProfessionalRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateProfessionalUseCase {
    private final ProfessionalRepository professionalRepository;
    private final ProfessionalRedisRepository professionalRedisRepository;

    @Transactional
    public void execute(UserRegisteredEvent event) {
        Professional professional = Professional.builder()
                .id(event.getUserId())
                .firstName(event.getFirstName())
                .lastName(event.getLastName())
                .email(event.getEmail())
                .active(true)
                .build();
        professionalRepository.save(professional);

        // Update Read Model (Redis)
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
