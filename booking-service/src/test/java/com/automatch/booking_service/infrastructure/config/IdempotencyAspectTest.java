package com.automatch.booking_service.infrastructure.config;

import com.automatch.booking_service.domain.model.IdempotencyRecord;
import com.automatch.booking_service.domain.repository.IdempotencyRecordRepository;
import com.automatch.booking_service.presentation.annotation.Idempotent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = IdempotencyAspectTest.TestController.class)
@Import({IdempotencyAspectTest.TestConfig.class, ObjectMapper.class})
public class IdempotencyAspectTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdempotencyRecordRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Configuration
    @EnableAspectJAutoProxy
    static class TestConfig {
        @Bean
        public IdempotencyAspect idempotencyAspect(IdempotencyRecordRepository repository, ObjectMapper objectMapper) {
            return new IdempotencyAspect(repository, objectMapper);
        }
        @Bean
        public TestController testController() {
            return new TestController();
        }
    }

    @RestController
    static class TestController {
        @PostMapping("/test-idempotent")
        @Idempotent
        public ResponseEntity<Map<String, String>> testMethod() {
            return ResponseEntity.ok(Map.of("status", "success"));
        }
    }

    @Test
    void shouldReturnBadRequestWhenKeyIsMissing() throws Exception {
        mockMvc.perform(post("/test-idempotent"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing Idempotency-Key header"));
    }

    @Test
    void shouldProceedAndSaveWhenKeyIsNew() throws Exception {
        String key = "new-key";
        when(repository.findById(key)).thenReturn(Optional.empty());

        mockMvc.perform(post("/test-idempotent")
                        .header("Idempotency-Key", key))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));

        verify(repository, times(1)).save(any(IdempotencyRecord.class));
    }

    @Test
    void shouldReturnCachedResponseWhenKeyExists() throws Exception {
        String key = "existing-key";
        IdempotencyRecord record = IdempotencyRecord.builder()
                .idempotencyKey(key)
                .requestPath("/test-idempotent")
                .responseStatus(200)
                .responseBody("{\"status\":\"cached\"}")
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.findById(key)).thenReturn(Optional.of(record));

        mockMvc.perform(post("/test-idempotent")
                        .header("Idempotency-Key", key))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("cached"));

        verify(repository, never()).save(any());
    }

    @Test
    void shouldHandleJsonProcessingExceptionDuringDeserialization() throws Exception {
        String key = "invalid-json-key";
        IdempotencyRecord record = IdempotencyRecord.builder()
                .idempotencyKey(key)
                .requestPath("/test-idempotent")
                .responseStatus(200)
                .responseBody("invalid-json")
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.findById(key)).thenReturn(Optional.of(record));

        // The aspect should catch JsonProcessingException and return the raw body
        mockMvc.perform(post("/test-idempotent")
                        .header("Idempotency-Key", key))
                .andExpect(status().isOk())
                .andExpect(content().string("invalid-json"));
    }
}
