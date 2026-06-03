package com.automatch.catalog_service.infrastructure.persistence;

import com.automatch.catalog_service.domain.model.Professional;
import com.automatch.catalog_service.domain.repository.ProfessionalRepository;
import com.automatch.catalog_service.infrastructure.persistence.entity.ProfessionalEntity;
import com.automatch.catalog_service.infrastructure.persistence.repository.JpaProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProfessionalRepositoryImpl implements ProfessionalRepository {
    private final JpaProfessionalRepository jpaProfessionalRepository;

    @Override
    public Professional save(Professional professional) {
        ProfessionalEntity entity = toEntity(professional);
        ProfessionalEntity saved = jpaProfessionalRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Professional> findById(UUID id) {
        return jpaProfessionalRepository.findById(id).map(this::toDomain);
    }

    private ProfessionalEntity toEntity(Professional p) {
        return ProfessionalEntity.builder()
                .id(p.getId())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .email(p.getEmail())
                .specialty(p.getSpecialty())
                .bio(p.getBio())
                .services(p.getServices())
                .active(p.isActive())
                .build();
    }

    private Professional toDomain(ProfessionalEntity e) {
        return Professional.builder()
                .id(e.getId())
                .firstName(e.getFirstName())
                .lastName(e.getLastName())
                .email(e.getEmail())
                .specialty(e.getSpecialty())
                .bio(e.getBio())
                .services(e.getServices())
                .active(e.isActive())
                .build();
    }
}
