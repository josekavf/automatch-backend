package com.automatch.catalog_service.presentation.controller;

import com.automatch.catalog_service.application.dto.UpdateProfessionalRequest;
import com.automatch.catalog_service.application.usecase.SearchProfessionalUseCase;
import com.automatch.catalog_service.application.usecase.UpdateProfessionalUseCase;
import com.automatch.catalog_service.infrastructure.cache.ProfessionalRedisEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/professionals")
@RequiredArgsConstructor
@Tag(name = "Catálogo de Profissionais", description = "Endpoints para busca e gestão de profissionais")
public class ProfessionalController {
    private final SearchProfessionalUseCase searchProfessionalUseCase;
    private final UpdateProfessionalUseCase updateProfessionalUseCase;
    private final com.automatch.catalog_service.application.usecase.GetProfessionalUseCase getProfessionalUseCase;

    @GetMapping("/{id}")
    @Operation(summary = "Obter profissional por ID", description = "Retorna os detalhes de um profissional específico")
    public ResponseEntity<com.automatch.catalog_service.domain.model.Professional> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(getProfessionalUseCase.execute(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar profissionais", description = "Busca profissionais por especialidade ou lista todos se nenhuma especialidade for fornecida")
    public ResponseEntity<List<ProfessionalRedisEntity>> search(@RequestParam(required = false) String specialty) {
        return ResponseEntity.ok(searchProfessionalUseCase.execute(specialty));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar profissional", description = "Atualiza os dados de um profissional e sincroniza com o catálogo")
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody UpdateProfessionalRequest request) {
        updateProfessionalUseCase.execute(id, request);
        return ResponseEntity.noContent().build();
    }
}
