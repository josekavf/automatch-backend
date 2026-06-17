package com.automatch.catalog_service.application.usecase;

import com.automatch.catalog_service.domain.model.Professional;
import com.automatch.catalog_service.domain.repository.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetProfessionalUseCase {
    private final ProfessionalRepository professionalRepository;

    public Professional execute(UUID id) {
        return professionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
    }
}
