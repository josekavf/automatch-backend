package com.automatch.catalog_service.infrastructure.persistence.repository;

import com.automatch.catalog_service.infrastructure.persistence.entity.ProfessionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface JpaProfessionalRepository extends JpaRepository<ProfessionalEntity, UUID> {
}
