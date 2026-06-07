package com.automatch.catalog_service.infrastructure.persistence;

import com.automatch.catalog_service.domain.model.Professional;
import com.automatch.catalog_service.infrastructure.persistence.entity.ProfessionalEntity;
import com.automatch.catalog_service.infrastructure.persistence.repository.JpaProfessionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessionalRepositoryImplTest {

    @Mock
    private JpaProfessionalRepository jpaProfessionalRepository;

    @InjectMocks
    private ProfessionalRepositoryImpl professionalRepository;

    private Professional professional;
    private ProfessionalEntity professionalEntity;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        professional = Professional.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .specialty("Mechanic")
                .bio("Experienced mechanic")
                .services(List.of("Oil change", "Brake repair"))
                .active(true)
                .build();

        professionalEntity = ProfessionalEntity.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .specialty("Mechanic")
                .bio("Experienced mechanic")
                .services(List.of("Oil change", "Brake repair"))
                .active(true)
                .build();
    }

    @Test
    void shouldSaveProfessional() {
        when(jpaProfessionalRepository.save(any(ProfessionalEntity.class))).thenReturn(professionalEntity);

        Professional saved = professionalRepository.save(professional);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(id);
        assertThat(saved.getFirstName()).isEqualTo("John");
        verify(jpaProfessionalRepository).save(any(ProfessionalEntity.class));
    }

    @Test
    void shouldFindProfessionalById() {
        when(jpaProfessionalRepository.findById(id)).thenReturn(Optional.of(professionalEntity));

        Optional<Professional> found = professionalRepository.findById(id);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(id);
        assertThat(found.get().getFirstName()).isEqualTo("John");
    }

    @Test
    void shouldReturnEmptyWhenProfessionalNotFound() {
        when(jpaProfessionalRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Professional> found = professionalRepository.findById(id);

        assertThat(found).isEmpty();
    }
}
