package com.automatch.catalog_service.application.service;

import com.automatch.catalog_service.domain.event.ProfessionalUpdatedEvent;

public interface EventPublisher {
    void publishProfessionalUpdated(ProfessionalUpdatedEvent event);
}
