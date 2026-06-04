package com.automatch.catalog_service.presentation.controller;

import com.automatch.catalog_service.application.usecase.SearchProfessionalUseCase;
import com.automatch.catalog_service.infrastructure.cache.ProfessionalRedisEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/professionals")
@RequiredArgsConstructor
@Tag(name = "Catálogo de Profissionais", description = "Endpoints para busca de profissionais")
public class ProfessionalController {
    private final SearchProfessionalUseCase searchProfessionalUseCase;

    @GetMapping("/search")
    @Operation(summary = "Buscar profissionais", description = "Busca profissionais por especialidade ou lista todos se nenhuma especialidade for fornecida")
    public ResponseEntity<List<ProfessionalRedisEntity>> search(@RequestParam(required = false) String specialty) {
        return ResponseEntity.ok(searchProfessionalUseCase.execute(specialty));
    }
}
