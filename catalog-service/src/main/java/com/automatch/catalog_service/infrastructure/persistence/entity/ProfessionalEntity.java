package com.automatch.catalog_service.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "professionals")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalEntity {
    @Id
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    private String specialty;
    private String bio;

    @ElementCollection
    @CollectionTable(name = "professional_services", joinColumns = @JoinColumn(name = "professional_id"))
    @Column(name = "service")
    private List<String> services;

    private boolean active;
}
