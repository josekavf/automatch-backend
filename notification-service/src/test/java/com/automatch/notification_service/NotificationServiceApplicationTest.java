package com.automatch.notification_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("test")
class NotificationServiceApplicationTest {

    @Test
    void contextLoads() {
        // Test if context loads correctly
    }

    @Test
    void mainMethodTest() {
        assertDoesNotThrow(() -> NotificationServiceApplication.main(new String[]{}));
    }
}
