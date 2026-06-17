package com.automatch.catalog_service.presentation.controller;

import com.automatch.catalog_service.application.usecase.SearchProfessionalUseCase;
import com.automatch.catalog_service.application.usecase.UpdateProfessionalUseCase;
import com.automatch.catalog_service.infrastructure.cache.ProfessionalRedisEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfessionalController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfessionalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchProfessionalUseCase searchProfessionalUseCase;

    @MockBean
    private UpdateProfessionalUseCase updateProfessionalUseCase;

    @MockBean
    private com.automatch.catalog_service.application.usecase.GetProfessionalUseCase getProfessionalUseCase;

    @Test
    void getById_ShouldReturnOk() throws Exception {
        UUID id = UUID.randomUUID();
        com.automatch.catalog_service.domain.model.Professional professional = com.automatch.catalog_service.domain.model.Professional.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .specialty("Mechanic")
                .build();

        when(getProfessionalUseCase.execute(id)).thenReturn(professional);

        mockMvc.perform(get("/api/v1/professionals/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.specialty").value("Mechanic"));
    }

    @Test
    void search_ShouldReturnOk() throws Exception {
        ProfessionalRedisEntity entity = ProfessionalRedisEntity.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .specialty("Mechanic")
                .build();

        when(searchProfessionalUseCase.execute(anyString())).thenReturn(List.of(entity));

        mockMvc.perform(get("/api/v1/professionals/search")
                .param("specialty", "Mechanic")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }
}
