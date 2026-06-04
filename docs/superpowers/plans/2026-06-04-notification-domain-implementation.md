# Notification Domain and Logic Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement the core domain, use case, and Kafka consumer for the notification service to process and log notification events.

**Architecture:** Hexagonal/Clean Architecture with Domain (Event DTO), Application (UseCase), and Infrastructure (Kafka Listener).

**Tech Stack:** Java 21, Spring Boot 3.2.5, Spring Kafka, Lombok, SLF4J.

---

### Task 1: Environment Setup

**Files:**
- Modify: `notification-service/src/main/resources/application.properties`

- [ ] **Step 1: Add Kafka bootstrap server for local development**
Update `notification-service/src/main/resources/application.properties` to include `spring.kafka.bootstrap-servers=localhost:9094`.

- [ ] **Step 2: Commit**
```bash
git add notification-service/src/main/resources/application.properties
git commit -m "chore(notification): add kafka bootstrap-servers for local dev"
```

### Task 2: Implement NotificationEvent DTO

**Files:**
- Create: `notification-service/src/main/java/com/automatch/notification_service/domain/event/NotificationEvent.java`

- [ ] **Step 1: Create the NotificationEvent DTO**
Define the `NotificationEvent` with `recipient` and `message` fields using Lombok.

```java
package com.automatch.notification_service.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private String recipient;
    private String message;
}
```

- [ ] **Step 2: Commit**
```bash
git add notification-service/src/main/java/com/automatch/notification_service/domain/event/NotificationEvent.java
git commit -m "feat(notification): add NotificationEvent domain DTO"
```

### Task 3: Implement ProcessNotificationUseCase (TDD)

**Files:**
- Create: `notification-service/src/main/java/com/automatch/notification_service/application/usecase/ProcessNotificationUseCase.java`
- Create: `notification-service/src/test/java/com/automatch/notification_service/application/usecase/ProcessNotificationUseCaseTest.java`

- [ ] **Step 1: Write the failing test**
Create `ProcessNotificationUseCaseTest` and verify it calls the log (indirectly by testing if the use case can be executed).

```java
package com.automatch.notification_service.application.usecase;

import com.automatch.notification_service.domain.event.NotificationEvent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ProcessNotificationUseCaseTest {

    @Test
    void shouldProcessNotificationWithoutErrors() {
        ProcessNotificationUseCase useCase = new ProcessNotificationUseCase();
        NotificationEvent event = NotificationEvent.builder()
                .recipient("test@example.com")
                .message("Hello Test")
                .build();

        assertDoesNotThrow(() -> useCase.execute(event));
    }
}
```

- [ ] **Step 2: Run test to verify it fails**
Run: `mvn test -pl notification-service -Dtest=ProcessNotificationUseCaseTest`
Expected: FAIL (Compilation error: ProcessNotificationUseCase not found)

- [ ] **Step 3: Write minimal implementation**
Create `ProcessNotificationUseCase` with SLF4J logging.

```java
package com.automatch.notification_service.application.usecase;

import com.automatch.notification_service.domain.event.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProcessNotificationUseCase {

    public void execute(NotificationEvent event) {
        log.info("Sending notification to: {} - Message: {}", event.getRecipient(), event.getMessage());
    }
}
```

- [ ] **Step 4: Run test to verify it passes**
Run: `mvn test -pl notification-service -Dtest=ProcessNotificationUseCaseTest`
Expected: PASS

- [ ] **Step 5: Commit**
```bash
git add notification-service/src/main/java/com/automatch/notification_service/application/usecase/ProcessNotificationUseCase.java notification-service/src/test/java/com/automatch/notification_service/application/usecase/ProcessNotificationUseCaseTest.java
git commit -m "feat(notification): implement ProcessNotificationUseCase with TDD"
```

### Task 4: Implement NotificationEventListener

**Files:**
- Create: `notification-service/src/main/java/com/automatch/notification_service/infrastructure/messaging/NotificationEventListener.java`

- [ ] **Step 1: Create the Kafka Listener**
Implement the listener to consume from `topic-notification-events`.

```java
package com.automatch.notification_service.infrastructure.messaging;

import com.automatch.notification_service.application.usecase.ProcessNotificationUseCase;
import com.automatch.notification_service.domain.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final ProcessNotificationUseCase processNotificationUseCase;

    @KafkaListener(topics = "topic-notification-events", groupId = "notification-service-group")
    public void listen(NotificationEvent event) {
        log.info("Received notification event for recipient: {}", event.getRecipient());
        processNotificationUseCase.execute(event);
    }
}
```

- [ ] **Step 2: Commit**
```bash
git add notification-service/src/main/java/com/automatch/notification_service/infrastructure/messaging/NotificationEventListener.java
git commit -m "feat(notification): add Kafka listener for notification events"
```

### Task 5: Final Validation

- [ ] **Step 1: Run all tests in notification-service**
Run: `mvn test -pl notification-service`
Expected: All tests pass.

- [ ] **Step 2: Verify project compilation**
Run: `mvn clean compile -pl notification-service`
Expected: SUCCESS
