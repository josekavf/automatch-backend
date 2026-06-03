package com.automatch.catalog_service.infrastructure.cache;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.UUID;

public interface ProfessionalRedisRepository extends CrudRepository<ProfessionalRedisEntity, UUID> {
    List<ProfessionalRedisEntity> findBySpecialty(String specialty);
}
