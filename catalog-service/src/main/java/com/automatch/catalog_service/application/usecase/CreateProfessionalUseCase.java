package com.automatch.catalog_service.application.usecase;

import com.automatch.catalog_service.domain.event.UserRegisteredEvent;
import com.automatch.catalog_service.domain.model.Professional;
import com.automatch.catalog_service.domain.repository.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateProfessionalUseCase {
    private final ProfessionalRepository professionalRepository;

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
    }
}
