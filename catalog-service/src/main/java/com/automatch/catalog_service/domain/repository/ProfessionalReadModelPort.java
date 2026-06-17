package com.automatch.catalog_service.domain.repository;

import com.automatch.catalog_service.domain.model.Professional;

public interface ProfessionalReadModelPort {
    void saveReadModel(Professional professional);
}
