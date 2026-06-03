package com.automatch.catalog_service.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalUpdatedEvent {
    private UUID id;
    private String firstName;
    private String lastName;
    private String specialty;
    private List<String> services;
    private boolean active;
}
