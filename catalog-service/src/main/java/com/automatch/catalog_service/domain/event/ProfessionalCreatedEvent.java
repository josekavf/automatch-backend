package com.automatch.catalog_service.domain.event;

import com.automatch.catalog_service.domain.model.Professional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfessionalCreatedEvent {
    private final Professional professional;
}
