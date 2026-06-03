package com.automatch.catalog_service.domain.repository;

import com.automatch.catalog_service.domain.model.Professional;
import java.util.Optional;
import java.util.UUID;

public interface ProfessionalRepository {
    Professional save(Professional professional);
    Optional<Professional> findById(UUID id);
}
