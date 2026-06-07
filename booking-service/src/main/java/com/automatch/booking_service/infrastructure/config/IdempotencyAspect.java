package com.automatch.booking_service.infrastructure.config;

import com.automatch.booking_service.domain.model.IdempotencyRecord;
import com.automatch.booking_service.domain.repository.IdempotencyRecordRepository;
import com.automatch.booking_service.presentation.annotation.Idempotent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class IdempotencyAspect {

    private final IdempotencyRecordRepository repository;
    private final ObjectMapper objectMapper;

    @Around("@annotation(idempotent)")
    public Object handleIdempotency(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String key = request.getHeader("Idempotency-Key");

        if (key == null || key.isBlank()) {
            log.warn("Missing Idempotency-Key header for idempotent request to {}", request.getRequestURI());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing Idempotency-Key header");
        }

        Optional<IdempotencyRecord> record = repository.findById(key);
        if (record.isPresent()) {
            log.info("Idempotency hit for key: {}", key);
            IdempotencyRecord r = record.get();
            Object body = null;
            if (r.getResponseBody() != null) {
                // We need to know the return type of the method to deserialize correctly
                // For simplicity, we can return the body as a Map or just the raw string if needed
                // But usually, ResponseEntity methods return a specific DTO.
                // Since we don't know the exact type here, we'll deserialize as Object (Map)
                try {
                    body = objectMapper.readValue(r.getResponseBody(), Object.class);
                } catch (JsonProcessingException e) {
                    log.error("Failed to deserialize idempotency response body", e);
                    body = r.getResponseBody();
                }
            }
            return ResponseEntity.status(r.getResponseStatus()).body(body);
        }

        Object result = joinPoint.proceed();

        if (result instanceof ResponseEntity<?> response) {
            try {
                String bodyJson = response.getBody() != null ? objectMapper.writeValueAsString(response.getBody()) : null;
                IdempotencyRecord newRecord = IdempotencyRecord.builder()
                        .idempotencyKey(key)
                        .requestPath(request.getRequestURI())
                        .responseStatus(response.getStatusCode().value())
                        .responseBody(bodyJson)
                        .createdAt(LocalDateTime.now())
                        .build();
                repository.save(newRecord);
                log.info("Saved idempotency record for key: {}", key);
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize response body for idempotency", e);
            }
        }

        return result;
    }
}
