package com.automatch.catalog_service.application.usecase;

import com.automatch.catalog_service.infrastructure.cache.ProfessionalRedisEntity;
import com.automatch.catalog_service.infrastructure.cache.ProfessionalRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class SearchProfessionalUseCase {
    private final ProfessionalRedisRepository professionalRedisRepository;

    public List<ProfessionalRedisEntity> execute(String specialty) {
        if (specialty != null && !specialty.isEmpty()) {
            return professionalRedisRepository.findBySpecialty(specialty);
        }
        return StreamSupport.stream(professionalRedisRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
