package com.automatch.iam_service.infrastructure.security;

import com.automatch.iam_service.domain.model.Role;
import com.automatch.iam_service.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenServiceImplTest {

    private JwtTokenServiceImpl jwtTokenService;
    private final String secret = "9a4f2c8d3b7a1e5f8g9h0j1k2l3m4n5o6p7q8r9s0t1u2v3w4x5y6z7a8b9c0d1e";
    private final long expiration = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtTokenService = new JwtTokenServiceImpl();
        ReflectionTestUtils.setField(jwtTokenService, "secret", secret);
        ReflectionTestUtils.setField(jwtTokenService, "expiration", expiration);
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .role(Role.CLIENT)
                .build();

        String token = jwtTokenService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals(user.getEmail(), claims.getSubject());
        assertEquals(user.getRole().name(), claims.get("role"));
        assertEquals(user.getFirstName(), claims.get("firstName"));
        assertEquals(user.getLastName(), claims.get("lastName"));
        assertTrue(claims.getExpiration().after(new Date()));
    }
}
