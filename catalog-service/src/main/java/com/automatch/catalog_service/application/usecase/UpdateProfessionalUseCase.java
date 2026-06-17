package com.automatch.catalog_service.application.usecase;

import com.automatch.catalog_service.application.dto.UpdateProfessionalRequest;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher applicationEventPublisher;

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

        // Publish internal event to sync Redis and Kafka safely after commit
        applicationEventPublisher.publishEvent(new com.automatch.catalog_service.domain.event.ProfessionalUpdatedEvent(
                professional.getId(),
                professional.getFirstName(),
                professional.getLastName(),
                professional.getSpecialty(),
                professional.getServices(),
                professional.isActive()
        ));
    }
}
