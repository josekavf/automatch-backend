# Test Coverage Improvement (90%) Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Reach 90% instruction coverage across all microservices by implementing missing tests and excluding boilerplates.

**Architecture:** Unit and Integration tests using JUnit 5, Mockito, and MockMvc. Lombok is configured to exclude generated code.

**Tech Stack:** Java 21, Spring Boot 3.2.5, JUnit 5, Mockito, JaCoCo.

---

### Task 1: Setup & Global Configuration

- [ ] **Step 1: Verify `lombok.config`**
Ensure the root `lombok.config` exists with the following content:
```config
config.stopBubbling = true
lombok.addLombokGeneratedAnnotation = true
```

- [ ] **Step 2: Commit global config**
```bash
git add lombok.config
git commit -m "test: exclude lombok generated code from coverage"
```

---

### Task 2: IAM Service Coverage

**Files:**
- Create: `iam-service/src/test/java/com/automatch/iam_service/infrastructure/persistence/UserRepositoryImplTest.java`
- Create: `iam-service/src/test/java/com/automatch/iam_service/infrastructure/security/JwtTokenServiceImplTest.java`
- Create: `iam-service/src/test/java/com/automatch/iam_service/presentation/exception/GlobalExceptionHandlerTest.java`

- [ ] **Step 1: Test `UserRepositoryImpl`**
Implement tests for mapping and JpaRepository interaction.

- [ ] **Step 2: Test `JwtTokenServiceImpl`**
Implement tests for token generation and claims verification.

- [ ] **Step 3: Test `GlobalExceptionHandler`**
Verify the traceId is present in the error response.

- [ ] **Step 4: Verify Coverage for IAM Service**
Run: `mvn clean verify -pl iam-service`
Verify JaCoCo report at `iam-service/target/site/jacoco/index.html`.

- [ ] **Step 5: Commit**
```bash
git add iam-service/src/test/java/com/automatch/iam_service/
git commit -m "test: increase iam-service coverage"
```

---

### Task 3: Catalog Service Coverage

**Files:**
- Create: `catalog-service/src/test/java/com/automatch/catalog_service/infrastructure/persistence/ProfessionalRepositoryImplTest.java`
- Create: `catalog-service/src/test/java/com/automatch/catalog_service/application/usecase/UpdateProfessionalUseCaseTest.java`
- Create: `catalog-service/src/test/java/com/automatch/catalog_service/infrastructure/messaging/ProfessionalEventListenerTest.java`

- [ ] **Step 1: Test `ProfessionalRepositoryImpl`**
- [ ] **Step 2: Test `UpdateProfessionalUseCase`**
- [ ] **Step 3: Test `ProfessionalEventListener`**
- [ ] **Step 4: Verify Coverage for Catalog Service**
Run: `mvn clean verify -pl catalog-service`

- [ ] **Step 5: Commit**
```bash
git add catalog-service/src/test/java/com/automatch/catalog_service/
git commit -m "test: increase catalog-service coverage"
```

---

### Task 4: Booking Service Coverage

**Files:**
- Create: `booking-service/src/test/java/com/automatch/booking_service/infrastructure/persistence/BookingRepositoryImplTest.java`
- Modify: `booking-service/src/test/java/com/automatch/booking_service/infrastructure/config/IdempotencyAspectTest.java`

- [ ] **Step 1: Test `BookingRepositoryImpl`**
- [ ] **Step 2: Test `GlobalExceptionHandler`**
- [ ] **Step 3: Verify Coverage for Booking Service**
Run: `mvn clean verify -pl booking-service`

- [ ] **Step 4: Commit**
```bash
git add booking-service/src/test/java/com/automatch/booking_service/
git commit -m "test: increase booking-service coverage"
```

---

### Task 5: Notification Service Coverage

**Files:**
- Create: `notification-service/src/test/java/com/automatch/notification_service/infrastructure/messaging/NotificationEventListenerTest.java`
- Create: `notification-service/src/test/java/com/automatch/notification_service/presentation/exception/GlobalExceptionHandlerTest.java`

- [ ] **Step 1: Test `NotificationEventListener`**
- [ ] **Step 2: Test `GlobalExceptionHandler`**
- [ ] **Step 3: Verify Coverage for Notification Service**
Run: `mvn clean verify -pl notification-service`

- [ ] **Step 4: Commit**
```bash
git add notification-service/src/test/java/com/automatch/notification_service/
git commit -m "test: increase notification-service coverage"
```

---

### Task 6: Final Verification

- [ ] **Step 1: Run full build and coverage extraction**
Run: `mvn clean verify`
Use Python script to verify all modules meet 90% instruction coverage.

- [ ] **Step 2: Final Commit**
```bash
git commit --allow-empty -m "test: finalized coverage improvements to 90%"
```
