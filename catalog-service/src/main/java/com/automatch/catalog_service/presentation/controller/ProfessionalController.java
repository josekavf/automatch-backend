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
@Tag(name = "Professional Catalog", description = "Endpoints for searching professionals")
public class ProfessionalController {
    private final SearchProfessionalUseCase searchProfessionalUseCase;

    @GetMapping("/search")
    @Operation(summary = "Search professionals", description = "Search for professionals by specialty or list all if no specialty is provided")
    public ResponseEntity<List<ProfessionalRedisEntity>> search(@RequestParam(required = false) String specialty) {
        return ResponseEntity.ok(searchProfessionalUseCase.execute(specialty));
    }
}
