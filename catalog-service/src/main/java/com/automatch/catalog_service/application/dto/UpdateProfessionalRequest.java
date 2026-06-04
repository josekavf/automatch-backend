package com.automatch.catalog_service.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfessionalRequest {
    private String firstName;
    private String lastName;
    private String specialty;
    private List<String> services;
    private boolean active;
}
