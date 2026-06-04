package com.automatch.catalog_service.application.usecase;

import com.automatch.catalog_service.application.dto.UpdateProfessionalRequest;
import com.automatch.catalog_service.application.service.EventPublisher;
import com.automatch.catalog_service.domain.event.ProfessionalUpdatedEvent;
import com.automatch.catalog_service.domain.model.Professional;
import com.automatch.catalog_service.domain.repository.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateProfessionalUseCase {
    private final ProfessionalRepository professionalRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public void execute(UUID id, UpdateProfessionalRequest request) {
        Professional professional = professionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));

        professional.setFirstName(request.getFirstName());
        professional.setLastName(request.getLastName());
        professional.setSpecialty(request.getSpecialty());
        professional.setServices(request.getServices());
        professional.setActive(request.isActive());

        professionalRepository.save(professional);

        eventPublisher.publishProfessionalUpdated(ProfessionalUpdatedEvent.builder()
                .id(professional.getId())
                .firstName(professional.getFirstName())
                .lastName(professional.getLastName())
                .specialty(professional.getSpecialty())
                .services(professional.getServices())
                .active(professional.isActive())
                .build());
    }
}
