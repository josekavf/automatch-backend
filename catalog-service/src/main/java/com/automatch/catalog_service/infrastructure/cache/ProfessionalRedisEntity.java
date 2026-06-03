package com.automatch.catalog_service.infrastructure.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("professionals")
public class ProfessionalRedisEntity {
    @Id
    private UUID id;
    private String firstName;
    private String lastName;
    @Indexed
    private String specialty;
    private List<String> services;
    private boolean active;
}
