package com.automatch.booking_service.infrastructure.persistence;

import com.automatch.booking_service.domain.model.IdempotencyRecord;
import com.automatch.booking_service.domain.repository.IdempotencyRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class IdempotencyRecordRepositoryTest {

    @Autowired
    private IdempotencyRecordRepository repository;

    @Test
    void shouldSaveAndFindIdempotencyRecord() {
        String key = "test-key";
        IdempotencyRecord record = IdempotencyRecord.builder()
                .idempotencyKey(key)
                .requestPath("/api/v1/bookings")
                .responseStatus(201)
                .responseBody("{\"id\":\"123\"}")
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(record);

        Optional<IdempotencyRecord> found = repository.findById(key);

        assertThat(found).isPresent();
        assertThat(found.get().getIdempotencyKey()).isEqualTo(key);
        assertThat(found.get().getRequestPath()).isEqualTo("/api/v1/bookings");
        assertThat(found.get().getResponseStatus()).isEqualTo(201);
        assertThat(found.get().getResponseBody()).isEqualTo("{\"id\":\"123\"}");
    }
}
