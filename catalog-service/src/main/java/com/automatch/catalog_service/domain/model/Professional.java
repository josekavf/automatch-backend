package com.automatch.catalog_service.domain.model;

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
public class Professional {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String specialty;
    private String bio;
    private List<String> services;
    private boolean active;
}
